package com.matthewtamlin.retrial.core

import io.reactivex.Completable

/**
 * Runs a Gradle task.
 */
interface TaskRunner {
  /**
   * Runs the task. Calls must be idempotent.
   */
  fun run(): Completable
}