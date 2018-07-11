package com.matthewtamlin.retrial.dependencies.saved

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Repository of [SavedDependency] instances.
 */
interface SavedDependenciesRepository {
  /**
   * Gets the current saved dependencies, or an empty set if there are none.
   */
  fun get(): Single<Set<SavedDependency>>

  /**
   * Saves a set of dependencies, overwriting all previously saved dependencies.
   */
  fun set(dependencies: Set<SavedDependency>): Completable
}