package com.matthewtamlin.retrial.core.recorddependencies

import com.matthewtamlin.retrial.hash.Sha512Hash
import com.matthewtamlin.retrial.hash.Sha512HashGenerator
import com.matthewtamlin.retrial.dependencies.DependencyKey
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.live.LiveDependency
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependency
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TaskRunnerTests {

  private val savedDependenciesRepository = Mockito.mock(SavedDependenciesRepository::class.java)

  private val liveDependenciesRepository = Mockito.mock(LiveDependenciesRepository::class.java)

  private val checksumGenerator = Mockito.mock(Sha512HashGenerator::class.java)

  private val taskRunner = TaskRunner(
      savedDependenciesRepository,
      liveDependenciesRepository,
      checksumGenerator)

  private lateinit var liveDependencyA: LiveDependency

  private lateinit var liveDependencyB: LiveDependency

  private lateinit var savedDependencyA: SavedDependency

  private lateinit var savedDependencyB: SavedDependency

  @BeforeEach
  fun setup() {
    val dependencyKeyA = Mockito.mock(DependencyKey::class.java)
    val dependencyKeyB = Mockito.mock(DependencyKey::class.java)

    val dependencyAChecksum = Sha512Hash("A")
    val dependencyBChecksum = Sha512Hash("B")

    liveDependencyA = LiveDependency(dependencyKeyA, Mockito.mock(File::class.java))
    liveDependencyB = LiveDependency(dependencyKeyB, Mockito.mock(File::class.java))

    savedDependencyA = SavedDependency(dependencyKeyA, dependencyAChecksum)
    savedDependencyB = SavedDependency(dependencyKeyB, dependencyBChecksum)

    whenever(checksumGenerator.generateHash(liveDependencyA.file)).thenReturn(Single.just(dependencyAChecksum))
    whenever(checksumGenerator.generateHash(liveDependencyB.file)).thenReturn(Single.just(dependencyBChecksum))
  }

  @Nested
  @DisplayName("run()")
  inner class RunTests {

    @Test
    @DisplayName("should record an empty set when there are no live dependencies")
    fun noDependencies() {
      whenever(savedDependenciesRepository.set(any())).thenReturn(Completable.complete())
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf()))

      taskRunner
          .run()
          .test()
          .assertComplete()

      verify(savedDependenciesRepository, times(1)).set(setOf())
    }

    @Test
    @DisplayName("should record the dependency when there is one live dependency")
    fun oneDependency() {
      whenever(savedDependenciesRepository.set(any())).thenReturn(Completable.complete())
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA)))

      taskRunner
          .run()
          .test()
          .assertComplete()

      verify(savedDependenciesRepository, times(1)).set(setOf(savedDependencyA))
    }

    @Test
    @DisplayName("should record all dependencies when there are two live dependencies")
    fun twoDependencies() {
      whenever(savedDependenciesRepository.set(any())).thenReturn(Completable.complete())
      whenever(liveDependenciesRepository.get()).thenReturn(Single.just(setOf(liveDependencyA, liveDependencyB)))

      taskRunner
          .run()
          .test()
          .assertComplete()

      verify(savedDependenciesRepository, times(1)).set(setOf(savedDependencyA, savedDependencyB))
    }
  }
}