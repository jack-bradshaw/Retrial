package com.matthewtamlin.retrial.dependencies.live

import io.reactivex.Single

interface LiveDependenciesRepository {
  fun get(): Single<Set<LiveDependency>>
}