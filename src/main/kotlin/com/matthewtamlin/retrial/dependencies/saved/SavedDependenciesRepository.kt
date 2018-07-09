package com.matthewtamlin.retrial.dependencies.saved

import io.reactivex.Completable
import io.reactivex.Single

interface SavedDependenciesRepository {
  fun get(): Single<Set<SavedDependency>>

  fun set(dependencies: Set<SavedDependency>): Completable
}