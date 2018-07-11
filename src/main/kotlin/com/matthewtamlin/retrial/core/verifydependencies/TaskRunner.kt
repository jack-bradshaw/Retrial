/*
 * Copyright 2018 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.hash.Sha512Hash
import com.matthewtamlin.retrial.hash.Sha512HashGenerator
import com.matthewtamlin.retrial.core.TaskRunner
import com.matthewtamlin.retrial.dependencies.DependencyKey
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toMap

/**
 * Runs the verify dependencies task.
 */
class TaskRunner(
    private val savedDependenciesRepository: SavedDependenciesRepository,
    private val liveDependenciesRepository: LiveDependenciesRepository,
    private val hashGenerator: Sha512HashGenerator,
    private val resultLogger: ResultLogger
) : TaskRunner {

  override fun run() = Single
      .zip(
          getSavedDependencies(),
          getLiveDependencies(),
          BiFunction(::compareDependencies))
      .flatMapCompletable { handleResult(it) }

  private fun getSavedDependencies(): Single<Map<DependencyKey, Sha512Hash>> = savedDependenciesRepository
      .get()
      .flatMapObservable { Observable.fromIterable(it) }
      .map { Pair(it.key, it.hash) }
      .toMap()

  private fun getLiveDependencies(): Single<Map<DependencyKey, Sha512Hash>> = liveDependenciesRepository
      .get()
      .flatMapObservable { Observable.fromIterable(it) }
      .flatMapSingle { dependency ->
        hashGenerator
            .generateHash(dependency.file)
            .map { checksum -> Pair(dependency, checksum) }
            .map { Pair(it.first.key, it.second) } // Only need the key, not the rest of the dependency
      }
      .toMap()

  private fun compareDependencies(
      saved: Map<DependencyKey, Sha512Hash>,
      live: Map<DependencyKey, Sha512Hash>
  ): DependencyDiff {

    val additional = live.keys - saved.keys
    val missing = saved.keys - live.keys
    val common = saved.keys.intersect(live.keys)
    val changed = common.filter { saved[it] != live[it] }.toSet()

    return DependencyDiff(additional, missing, changed)
  }

  private fun handleResult(diff: DependencyDiff) = Completable.defer {
    if (diff.empty) {
      resultLogger.logSuccess()
    } else {
      resultLogger
          .logFailureDueTo(diff)
          .doOnComplete { throw VerificationFailedException() }
    }
  }
}