package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.checksum.Sha512Checksum
import com.matthewtamlin.retrial.checksum.Sha512ChecksumGenerator
import com.matthewtamlin.retrial.core.Crasher
import com.matthewtamlin.retrial.dependencies.DependencyKey
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.live.LiveDependency
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependency
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TaskRunnerTests {

  private val savedDependenciesRepository = mock(SavedDependenciesRepository::class.java)

  private val liveDependenciesRepository = mock(LiveDependenciesRepository::class.java)

  private val checksumGenerator = mock(Sha512ChecksumGenerator::class.java)

  private val logger = mock(ResultLogger::class.java)

  private val taskRunner = TaskRunner(
      savedDependenciesRepository,
      liveDependenciesRepository,
      checksumGenerator,
      logger)

  private lateinit var liveDependencyA: LiveDependency

  private lateinit var liveDependencyB: LiveDependency

  private lateinit var liveDependencyC: LiveDependency

  private lateinit var modifiedLiveDependencyA: LiveDependency

  private lateinit var modifiedLiveDependencyB: LiveDependency

  private lateinit var modifiedLiveDependencyC: LiveDependency

  private lateinit var savedDependencyA: SavedDependency

  private lateinit var savedDependencyB: SavedDependency

  private lateinit var savedDependencyC: SavedDependency

  @BeforeEach
  fun setup() {
    val dependencyKeyA = mock(DependencyKey::class.java)
    val dependencyKeyB = mock(DependencyKey::class.java)
    val dependencyKeyC = mock(DependencyKey::class.java)

    val dependencyAChecksum = Sha512Checksum("A")
    val dependencyBChecksum = Sha512Checksum("B")
    val dependencyCChecksum = Sha512Checksum("C")

    liveDependencyA = LiveDependency(dependencyKeyA, mock(File::class.java))
    liveDependencyB = LiveDependency(dependencyKeyB, mock(File::class.java))
    liveDependencyC = LiveDependency(dependencyKeyC, mock(File::class.java))

    modifiedLiveDependencyA = LiveDependency(dependencyKeyA, mock(File::class.java))
    modifiedLiveDependencyB = LiveDependency(dependencyKeyB, mock(File::class.java))
    modifiedLiveDependencyC = LiveDependency(dependencyKeyC, mock(File::class.java))

    savedDependencyA = SavedDependency(dependencyKeyA, dependencyAChecksum)
    savedDependencyB = SavedDependency(dependencyKeyB, dependencyBChecksum)
    savedDependencyC = SavedDependency(dependencyKeyC, dependencyCChecksum)

    whenever(checksumGenerator.generateChecksum(liveDependencyA.file)).thenReturn(Single.just(dependencyAChecksum))
    whenever(checksumGenerator.generateChecksum(liveDependencyB.file)).thenReturn(Single.just(dependencyBChecksum))
    whenever(checksumGenerator.generateChecksum(liveDependencyC.file)).thenReturn(Single.just(dependencyCChecksum))

    whenever(checksumGenerator.generateChecksum(modifiedLiveDependencyA.file))
        .thenReturn(Single.just(Sha512Checksum("A modified")))
    whenever(checksumGenerator.generateChecksum(modifiedLiveDependencyB.file))
        .thenReturn(Single.just(Sha512Checksum("B modified")))
    whenever(checksumGenerator.generateChecksum(modifiedLiveDependencyC.file))
        .thenReturn(Single.just(Sha512Checksum("C modified")))

    whenever(logger.logSuccess()).thenReturn(Completable.complete())
    whenever(logger.logFailureDueTo(any())).thenReturn(Completable.complete())
  }

  /**
   * Tests to ensure the [TaskRunner.run] method functions correctly, specifically that it applies the correct logic
   * when comparing live dependencies with saved dependencies.
   *
   * The tests can be summarised as:
   *
   * | Live | Saved | Result
   * | ---- |-------| -----------------------------------|
   * | -    | -     | Pass                               |
   * | A    | -     | Additional (A)                     |
   * | -    | A     | Missing (A)                        |
   * | A'   | A     | Changed (A)                        |
   * | A    | A     | Pass                               |
   * | AB   | -		  | Additional (A and B)               |
   * | -    | AB    | Missing (A and B)                  |
   * | A'B' | AB    | Changed (A and B)                  |
   * | AB   | AB    | Pass                               |
   * | AB   | A     | Additional (B)                     |
   * | A    | AB    | Missing (B)                        |
   * | AB'  | AB    | Changed (B)                        |
   * | AC'  | BC    | Added (A) Missing (B), Changed (C) |
   */
  @Nested
  @DisplayName("run")
  inner class RunTests {

    @Test
    @DisplayName("should log success when there are no dependencies")
    fun noDependencies() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf()))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf()))

      runTestWithSuccessExpected()
    }

    @Test
    @DisplayName("should log failure and crash when there is one additional dependency")
    fun oneAdded() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf()))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA)))

      runTestWithFailureExpected(DependencyDiff(additionalDependencies = setOf(liveDependencyA.key)))
    }

    @Test
    @DisplayName("should log failure and crash when there is one missing dependency")
    fun oneMissing() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf()))

      runTestWithFailureExpected(DependencyDiff(missingDependencies = setOf(liveDependencyA.key)))
    }

    @Test
    @DisplayName("should log failure and crash when there is one changed dependency")
    fun oneChanged() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(modifiedLiveDependencyA)))

      runTestWithFailureExpected(DependencyDiff(changedDependencies = setOf(liveDependencyA.key)))
    }

    @Test
    @DisplayName("should log success when there is one unchanged dependency")
    fun oneUnchanged() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA)))

      runTestWithSuccessExpected()
    }

    @Test
    @DisplayName("should log failure and crash when there are two additional dependencies")
    fun twoAdded() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf()))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA, liveDependencyB)))

      runTestWithFailureExpected(
          DependencyDiff(additionalDependencies = setOf(liveDependencyA.key, liveDependencyB.key)))
    }

    @Test
    @DisplayName("should log failure and crash when there are two missing dependencies")
    fun twoMissing() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA, savedDependencyB)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf()))

      runTestWithFailureExpected(DependencyDiff(missingDependencies = setOf(liveDependencyA.key, liveDependencyB.key)))
    }

    @Test
    @DisplayName("should log failure and crash when there are two changed dependencies")
    fun twoChanged() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA, savedDependencyB)))
      whenever(liveDependenciesRepository.get())
          .thenReturn(Single.just(setOf(modifiedLiveDependencyA, modifiedLiveDependencyB)))

      runTestWithFailureExpected(DependencyDiff(changedDependencies = setOf(liveDependencyA.key, liveDependencyB.key)))
    }

    @Test
    @DisplayName("should log success when there are two unchanged dependencies")
    fun twoUnchanged() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA, savedDependencyB)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA, liveDependencyB)))

      runTestWithSuccessExpected()
    }

    @Test
    @DisplayName("should log failure and crash when there is one unchanged dependency and one additional dependency")
    fun oneUnchangedOneAdded() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA, liveDependencyB)))

      runTestWithFailureExpected(DependencyDiff(additionalDependencies = setOf(liveDependencyB.key)))
    }

    @Test
    @DisplayName("should log failure and crash when there is one unchanged dependency and one missing dependency")
    fun oneUnchangedOneMissing() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA, savedDependencyB)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA)))

      runTestWithFailureExpected(DependencyDiff(missingDependencies = setOf(liveDependencyB.key)))
    }

    @Test
    @DisplayName("should log failure and crash when there is one unchanged dependency and one changed dependency")
    fun oneUnchangedOneChanged() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyA)))
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(modifiedLiveDependencyA)))

      runTestWithFailureExpected(DependencyDiff(changedDependencies = setOf(liveDependencyA.key)))
    }

    @Test
    @DisplayName(
        "should log failure and crash when there is one additional dependency, one missing dependency, and one " +
            "changed dependency")
    fun oneAddedOneMissingOneChanged() {
      whenever(savedDependenciesRepository.get()).thenReturn(Single.just(setOf(savedDependencyB, savedDependencyC)))
      whenever(liveDependenciesRepository.get())
          .thenReturn(Single.just(setOf(liveDependencyA, modifiedLiveDependencyC)))

      runTestWithFailureExpected(
          DependencyDiff(
              additionalDependencies = setOf(liveDependencyA.key),
              missingDependencies = setOf(liveDependencyB.key),
              changedDependencies = setOf(liveDependencyC.key)))
    }

    private fun runTestWithSuccessExpected() {
      taskRunner
          .run()
          .test()
          .assertComplete()

      verify(logger, times(1)).logSuccess()
      verify(logger, never()).logFailureDueTo(any())
    }

    private fun runTestWithFailureExpected(expectedDiff: DependencyDiff) {
      taskRunner
          .run()
          .test()
          .assertError(VerificationFailedException::class.java)

      verify(logger, never()).logSuccess()
      verify(logger, times(1)).logFailureDueTo(expectedDiff)
    }
  }
}