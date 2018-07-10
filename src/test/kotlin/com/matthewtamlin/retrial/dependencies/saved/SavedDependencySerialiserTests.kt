package com.matthewtamlin.retrial.dependencies.saved

import com.google.gson.Gson
import com.matthewtamlin.retrial.checksum.Sha512Checksum
import com.matthewtamlin.retrial.dependencies.DependencyKey
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SavedDependencySerialiserTests {

  private val serialiser = SavedDependencySerialiser(Gson())

  @Nested
  @DisplayName("serialise() then deserialise()")
  inner class SerialiseThenDeserialise {

    @Test
    @DisplayName("should return the original set when no dependencies are supplied")
    fun noDependencies() {
      runTestWithDependencies(setOf())

    }

    @Test
    @DisplayName("should return the original set when one dependency is supplied")
    fun oneDependency() {
      val dependencies = setOf(
          SavedDependency(DependencyKey("group", "name", "version", "filepath"), Sha512Checksum("hash")))

      runTestWithDependencies(dependencies)
    }

    @Test
    @DisplayName("should return the original set when two dependencies are supplied")
    fun twoDependencies(){
      val dependencies = setOf(
          SavedDependency(DependencyKey("group1", "name1", "version1", "filepath1"), Sha512Checksum("hash1")),
          SavedDependency(DependencyKey("group2", "name2", "version2", "filepath2"), Sha512Checksum("hash2")))


      runTestWithDependencies(dependencies)
    }

    private fun runTestWithDependencies(dependencies: Set<SavedDependency>) {
      serialiser
          .serialise(dependencies)
          .flatMap(serialiser::deserialise)
          .test()
          .assertResult(dependencies)
    }
  }
}