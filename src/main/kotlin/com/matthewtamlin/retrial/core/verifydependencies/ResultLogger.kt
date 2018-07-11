package com.matthewtamlin.retrial.core.verifydependencies

import io.reactivex.Completable

/**
 * Logs the results of the verify dependencies task.
 */
interface ResultLogger {
  /**
   * Logs the success of the verify dependencies task.
   */
  fun logSuccess(): Completable

  /**
   * Logs the failure of the verify dependencies task. The [diff] details the exact reason for the failure.
   */
  fun logFailureDueTo(diff: DependencyDiff): Completable
}