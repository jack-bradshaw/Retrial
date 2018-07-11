package com.matthewtamlin.retrial.core.recorddependencies

import com.matthewtamlin.retrial.hash.Sha512HashGenerator
import com.matthewtamlin.retrial.core.TaskRunner
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependency
import io.reactivex.Observable

class TaskRunner(
    private val savedDependenciesRepository: SavedDependenciesRepository,
    private val liveDependenciesRepository: LiveDependenciesRepository,
    private val hashGenerator: Sha512HashGenerator
) : TaskRunner {

  override fun run() = liveDependenciesRepository
      .get()
      .flatMapObservable { Observable.fromIterable(it) }
      .flatMapSingle { dependency ->
        hashGenerator
            .generateChecksum(dependency.file)
            .map { checksum -> SavedDependency(dependency.key, checksum) }
      }
      .collectInto(HashSet<SavedDependency>()) { set, dependency -> set.add(dependency) }
      .flatMapCompletable { savedDependenciesRepository.set(it) }
}