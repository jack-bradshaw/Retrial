package com.matthewtamlin.retrial.dependencies.live

import io.reactivex.Single

/**
 * Repository of [LiveDependency] instances.
 */
interface LiveDependenciesRepository {
  /**
   * Gets the current live dependencies, or an empty set if there are none.
   */
  fun get(): Single<Set<LiveDependency>>
}