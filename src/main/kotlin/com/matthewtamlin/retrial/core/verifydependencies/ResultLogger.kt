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