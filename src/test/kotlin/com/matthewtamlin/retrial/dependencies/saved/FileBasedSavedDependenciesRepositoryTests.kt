package com.matthewtamlin.retrial.dependencies.saved

import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.nhaarman.mockitokotlin2.whenever
import io.github.glytching.junit.extension.folder.TemporaryFolder
import io.github.glytching.junit.extension.folder.TemporaryFolderExtension
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(TemporaryFolderExtension::class)
class FileBasedSavedDependenciesRepositoryTests {

  private lateinit var file: File

  private lateinit var mockSerialiser: SavedDependencySerialiser

  private lateinit var repository: FileBasedSavedDependenciesRepository

  @BeforeEach
  fun setup(temporaryFolder: TemporaryFolder) {
    file = File(temporaryFolder.root, "test file")
    mockSerialiser = mock(SavedDependencySerialiser::class.java)
    repository = FileBasedSavedDependenciesRepository(file, mockSerialiser)
  }

  @Nested
  @DisplayName("set()")
  inner class SetTests {

    @Test
    @DisplayName("should create the file and then save the dependencies when the file does not exist")
    fun fileDoesNotExist() {
      val dependencies = setOf(mock(SavedDependency::class.java))
      val serialisedDependencies = "serialised dependencies"

      whenever(mockSerialiser.serialise(dependencies)).thenReturn(Single.just(serialisedDependencies))

      repository
          .set(dependencies)
          .test()
          .assertComplete()

      assertk.assert(file.exists()).isTrue()
      assertk.assert(file.readText()).isEqualTo(serialisedDependencies)
    }

    @Test
    @DisplayName("should save the dependencies when the file exists")
    fun fileExists() {
      val dependencies = setOf(mock(SavedDependency::class.java))
      val serialisedDependencies = "serialised dependencies"

      whenever(mockSerialiser.serialise(dependencies)).thenReturn(Single.just(serialisedDependencies))

      file.createNewFile()

      repository
          .set(dependencies)
          .test()
          .assertComplete()

      assertk.assert(file.exists()).isTrue()
      assertk.assert(file.readText()).isEqualTo(serialisedDependencies)
    }
  }

  @Nested
  @DisplayName("get()")
  inner class GetTests {

    @Test
    @DisplayName("should supply an empty set when the file does not exist")
    fun fileDoesNotExist() {
      repository
          .get()
          .test()
          .assertResult(setOf())
    }

    @Test
    @DisplayName("should supply the saved dependencies when the file exists")
    fun fileExists() {
      val dependencies = setOf(mock(SavedDependency::class.java))
      val serialisedDependencies = "serialised dependencies"

      whenever(mockSerialiser.deserialise(serialisedDependencies)).thenReturn(Single.just(dependencies))

      file.createNewFile()
      file.writeText(serialisedDependencies)

      repository
          .get()
          .test()
          .assertResult(dependencies)
    }
  }
}