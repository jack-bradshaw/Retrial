package com.matthewtamlin.retrial.core.verifydependencies

import io.reactivex.Completable

interface ResultLogger {
  fun logSuccess(): Completable

  fun logFailureDueTo(diff: DependencyDiff): Completable
}