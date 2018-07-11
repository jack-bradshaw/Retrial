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

import com.matthewtamlin.retrial.dependencies.DependencyKey

/**
 * The difference between the saved dependencies and the live dependencies of a project.
 *
 * @property additionalDependencies
 * the dependencies that are present in the live dependencies but not the saved dependencies
 *
 * @property missingDependencies
 * the dependencies that are present in the saved dependencies but not the live dependencies
 *
 * @property changedDependencies
 * the dependencies that are present in both the live dependencies and the saved dependencies, but with different
 * contents
 */
data class DependencyDiff(
    val additionalDependencies: Set<DependencyKey> = setOf(),
    val missingDependencies: Set<DependencyKey> = setOf(),
    val changedDependencies: Set<DependencyKey> = setOf()
) {

  /**
   * True if there are no additional dependencies, no missing dependencies and no changed dependencies, false otherwise.
   */
  val empty = additionalDependencies.isEmpty() && missingDependencies.isEmpty() && changedDependencies.isEmpty()
}