package com.matthewtamlin.retrial.core.recorddependencies

import com.matthewtamlin.retrial.checksum.Sha512ChecksumGenerator
import com.matthewtamlin.retrial.core.Crasher
import com.matthewtamlin.retrial.core.TaskRunner
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependency
import io.reactivex.Observable

class TaskRunner(
    private val savedDependenciesRepository: SavedDependenciesRepository,
    private val liveDependenciesRepository: LiveDependenciesRepository,
    private val checksumGenerator: Sha512ChecksumGenerator,
    private val resultLogger: ResultLogger,
    private val crasher: Crasher
) : TaskRunner {

  override fun createTask() = liveDependenciesRepository
      .get()
      .flatMapObservable { Observable.fromIterable(it) }
      .flatMapSingle { dependency ->
        checksumGenerator
            .generateChecksum(dependency.file)
            .map { checksum -> SavedDependency(dependency.key, checksum) }
      }
      .collectInto(HashSet<SavedDependency>()) { set, dependency -> set.add(dependency) }
      .flatMapCompletable { savedDependenciesRepository.set(it) }
      .andThen(resultLogger.logSuccess())
      .onErrorResumeNext { resultLogger.logFailure().andThen(crasher.failBuild()) }
}