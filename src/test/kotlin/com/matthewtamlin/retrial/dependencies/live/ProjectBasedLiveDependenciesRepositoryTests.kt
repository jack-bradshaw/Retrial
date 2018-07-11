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

package com.matthewtamlin.retrial.dependencies.live

import com.matthewtamlin.retrial.dependencies.DependencyKey
import io.github.glytching.junit.extension.folder.TemporaryFolder
import io.github.glytching.junit.extension.folder.TemporaryFolderExtension
import org.gradle.api.Project
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File

/**
 * Unit tests for [ProjectBasedLiveDependenciesRepository].
 *
 * These test are convoluted because the repository depends on [Project], and the only way to get a project instance is
 * to create a build script and pass it to the [GradleRunner]. Furthermore, the build script must contain the
 * invocation and assertion logic for the tests. A local maven repository is used to store testing dependencies,
 * to enable fast and reproducible testing. The dependencies are:
 * * com.matthew-tamlin:lib-0:1.0.0 &nbsp;&nbsp;&nbsp;&nbsp; Library with no dependencies.
 * * com.matthew-tamlin:lib-1:1.0.0 &nbsp;&nbsp;&nbsp;&nbsp; Library with a dependency on lib 0.
 * * com.matthew-tamlin:lib-2:1.0.0 &nbsp;&nbsp;&nbsp;&nbsp; Library with a dependency on lib 1, and thus
 * transitively lib-0.
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(TemporaryFolderExtension::class)
class ProjectBasedLiveDependenciesRepositoryTests {

  private lateinit var projectDirectory: File

  private lateinit var buildFile: File

  private lateinit var mavenRepository: File

  @BeforeEach
  fun setup(temporaryFolder: TemporaryFolder) {
    projectDirectory = temporaryFolder.createDirectory("build-dir")
    buildFile = File(projectDirectory, "build.gradle").apply { createNewFile() }
    mavenRepository = File("${File("").absolutePath}/src/test/testing-dependencies/repository")
  }

  @Nested
  @DisplayName("get()")
  inner class GetTests {

    @Test
    @DisplayName("should return an empty set when the project has no dependencies")
    fun noDependencies() {
      val dependencyKey = "com.matthew-tamlin:lib-0:1.0.0"

      val expectedLiveDependencies = setOf(
          LiveDependency(
              DependencyKey("com", "matthew-tamlin", "lib-0", "lib-0-1.0.0.jar"),
              File(mavenRepository, "com/matthew-tamlin/lib-0/1.0.0/lib-0-1.0.0.jar")))

      runTestWith(dependencyKey, expectedLiveDependencies)
    }

    @Test
    @DisplayName("should return a set containing the dependency when the project has one dependency")
    fun oneDependency() {
      val dependencyKey = "com.matthew-tamlin:lib-1:1.0.0"

      val expectedLiveDependencies = setOf(
          LiveDependency(
              DependencyKey("com", "matthew-tamlin", "lib-0", "lib-0-1.0.0.jar"),
              File(mavenRepository, "com/matthew-tamlin/lib-0/1.0.0/lib-0-1.0.0.jar")),
          LiveDependency(
              DependencyKey("com", "matthew-tamlin", "lib-1", "lib-1-1.0.0.jar"),
              File(mavenRepository, "com/matthew-tamlin/lib-1/1.0.0/lib-1-1.0.0.jar")))

      runTestWith(dependencyKey, expectedLiveDependencies)
    }

    @Test
    @DisplayName("should return a set containing all dependencies when the project has two dependency")
    fun twoDependencies() {
      val dependencyKey = "com.matthew-tamlin:lib-2:1.0.0"

      val expectedLiveDependencies = setOf(
          LiveDependency(
              DependencyKey("com", "matthew-tamlin", "lib-0", "lib-0-1.0.0.jar"),
              File(mavenRepository, "com/matthew-tamlin/lib-0/1.0.0/lib-0-1.0.0.jar")),
          LiveDependency(
              DependencyKey("com", "matthew-tamlin", "lib-1", "lib-1-1.0.0.jar"),
              File(mavenRepository, "com/matthew-tamlin/lib-1/1.0.0/lib-1-1.0.0.jar")),
          LiveDependency(
              DependencyKey("com", "matthew-tamlin", "lib-2", "lib-2-1.0.0.jar"),
              File(mavenRepository, "com/matthew-tamlin/lib-2/1.0.0/lib-2-1.0.0.jar")))

      runTestWith(dependencyKey, expectedLiveDependencies)
    }

    private fun runTestWith(dependencyKey: String, expectedLiveDependencies: Set<LiveDependency>) {
      createBuildToRunTestWith(dependencyKey, expectedLiveDependencies)

      GradleRunner
          .create()
          .withProjectDir(projectDirectory)
          .withArguments("testTask")
          .withPluginClasspath()
          .build()
    }

    private fun createBuildToRunTestWith(dependency: String, expected: Set<LiveDependency>) {
      val fileContents = """
        plugins {
          id 'retrial'
        }

        apply plugin: 'java'

        sourceCompatibility = 1.8

        repositories {
          maven { url 'file://${mavenRepository.absolutePath}' }
        }

        dependencies {
          implementation '$dependency'
        }

        task testTask {
          doLast {
            def repo = new com.matthewtamlin.retrial.dependencies.live.ProjectBasedLiveDependenciesRepository(project)

            repo
              .get()
              .test()
              .assertValues(${toGroovySet(expected)} as Set)
          }
        }
      """.trimIndent()

      buildFile.writeText(fileContents)
    }

    private fun toGroovySet(liveDependencies: Set<LiveDependency>): String {
      val groovyDependencies = HashSet<String>()

      for (dependency in liveDependencies) {
        groovyDependencies.add(
            """
          new com.matthewtamlin.retrial.dependencies.live.LiveDependency(
            new com.matthewtamlin.retrial.dependencies.DependencyKey("${dependency.key.group}", "${dependency.key.name}", "${dependency.key.version}", "${dependency.key.filepath}"),
            new File("${dependency.file.absolutePath}"))""")
      }

      return groovyDependencies.toString()
    }
  }
}