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

import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.matthewtamlin.retrial.dependencies.DependencyKey
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DependencyDiffTests {

  @Nested
  @DisplayName("empty")
  inner class EmptyTests {

    @Test
    @DisplayName("should be true when there are no additional, missing or changed dependencies")
    fun noAdditionalMissingOrChangedDependencies() {
      val diff = DependencyDiff(
          additionalDependencies = setOf(),
          missingDependencies = setOf(),
          changedDependencies = setOf())

      assertk.assert(diff.empty).isTrue()
    }

    @Test
    @DisplayName("should be false when there are additional dependencies")
    fun additionalDependencies() {
      val diff = DependencyDiff(additionalDependencies = setOf(mock(DependencyKey::class.java)))

      assertk.assert(diff.empty).isFalse()
    }

    @Test
    @DisplayName("should be false when there are missing dependencies")
    fun missingDependencies() {
      val diff = DependencyDiff(missingDependencies = setOf(mock(DependencyKey::class.java)))

      assertk.assert(diff.empty).isFalse()
    }

    @Test
    @DisplayName("should be false when there are changed dependencies")
    fun changedDependencies() {
      val diff = DependencyDiff(changedDependencies = setOf(mock(DependencyKey::class.java)))

      assertk.assert(diff.empty).isFalse()
    }
  }
}