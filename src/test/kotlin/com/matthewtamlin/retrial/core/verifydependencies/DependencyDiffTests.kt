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
  @DisplayName("successful")
  inner class SuccessfulTests {

    @Test
    @DisplayName("should be true when there are no additional, missing or changed dependencies")
    fun noAdditionalMissingOrChangedDependencies() {
      val diff = DependencyDiff(
          additionalDependencies = setOf(),
          missingDependencies = setOf(),
          changedDependencies = setOf())

      assertk.assert(diff.successful).isTrue()
    }

    @Test
    @DisplayName("should be false when there are additional dependencies")
    fun additionalDependencies() {
      val diff = DependencyDiff(additionalDependencies = setOf(mock(DependencyKey::class.java)))

      assertk.assert(diff.successful).isFalse()
    }

    @Test
    @DisplayName("should be false when there are missing dependencies")
    fun missingDependencies() {
      val diff = DependencyDiff(missingDependencies = setOf(mock(DependencyKey::class.java)))

      assertk.assert(diff.successful).isFalse()
    }

    @Test
    @DisplayName("should be false when there are changed dependencies")
    fun changedDependencies() {
      val diff = DependencyDiff(changedDependencies = setOf(mock(DependencyKey::class.java)))

      assertk.assert(diff.successful).isFalse()
    }
  }
}