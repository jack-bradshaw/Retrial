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