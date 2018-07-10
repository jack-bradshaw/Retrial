package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.checksum.Sha512Checksum
import com.matthewtamlin.retrial.checksum.Sha512ChecksumGenerator
import com.matthewtamlin.retrial.core.Crasher
import com.matthewtamlin.retrial.core.TaskRunner
import com.matthewtamlin.retrial.dependencies.DependencyKey
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toMap

class TaskRunner(
    private val savedDependenciesRepository: SavedDependenciesRepository,
    private val liveDependenciesRepository: LiveDependenciesRepository,
    private val checksumGenerator: Sha512ChecksumGenerator,
    private val resultLogger: ResultLogger,
    private val crasher: Crasher
) : TaskRunner {

  override fun createTask() = Single
      .zip(
          getSavedDependencies(),
          getLiveDependencies(),
          BiFunction(::compareDependencies))
      .flatMapCompletable { handleResult(it) }

  private fun getSavedDependencies(): Single<Map<DependencyKey, Sha512Checksum>> = savedDependenciesRepository
      .get()
      .flatMapObservable { Observable.fromIterable(it) }
      .map { Pair(it.key, it.checksum) }
      .toMap()

  private fun getLiveDependencies(): Single<Map<DependencyKey, Sha512Checksum>> = liveDependenciesRepository
      .get()
      .flatMapObservable { Observable.fromIterable(it) }
      .flatMapSingle { dependency ->
        checksumGenerator
            .generateChecksum(dependency.file)
            .map { checksum -> Pair(dependency, checksum) }
            .map { Pair(it.first.key, it.second) } // Only need the key, not the rest of the dependency
      }
      .toMap()

  private fun compareDependencies(
      saved: Map<DependencyKey, Sha512Checksum>,
      live: Map<DependencyKey, Sha512Checksum>
  ): DependencyDiff {

    val additional = live.keys - saved.keys
    val missing = saved.keys - live.keys
    val common = saved.keys.intersect(live.keys)
    val changed = common.filter { saved[it] != live[it] }.toSet()

    return DependencyDiff(additional, missing, changed)
  }

  private fun handleResult(diff: DependencyDiff) = Completable.defer {
    if (diff.successful) {
      resultLogger.logSuccess()
    } else {
      resultLogger.logFailureDueTo(diff).andThen(crasher.failBuild())
    }
  }
}