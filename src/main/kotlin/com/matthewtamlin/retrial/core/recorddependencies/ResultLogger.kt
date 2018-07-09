package com.matthewtamlin.retrial.core.recorddependencies

import io.reactivex.Completable

interface ResultLogger {
  fun logSuccess(): Completable

  fun logFailure(): Completable
}