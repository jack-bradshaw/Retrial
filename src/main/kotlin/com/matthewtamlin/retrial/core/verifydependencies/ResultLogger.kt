package com.matthewtamlin.retrial.core.verifydependencies

import io.reactivex.Completable

/**
 * Logs the results of the verify dependencies task.
 */
interface ResultLogger {
  /**
   * Logs a success of the verify dependencies task.
   */
  fun logSuccess(): Completable

  /**
   * Logs a failure of the verify dependencies task.
   *
   * @param diff details the exact reason for the failure.
   */
  fun logFailureDueTo(diff: DependencyDiff): Completable
}