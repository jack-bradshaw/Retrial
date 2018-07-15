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
import io.reactivex.Observable
import io.reactivex.Single
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import javax.inject.Inject

/**
 * [LiveDependenciesRepository] that sources dependencies from [project].
 */
class ProjectBasedLiveDependenciesRepository @Inject constructor(
    private val project: Project
) : LiveDependenciesRepository {

  private val projectPath: String = project.path

  override fun get(): Single<Set<LiveDependency>> = Observable
      .fromIterable(project.configurations)
      .filter { it.isCanBeResolved }
      .map(::getDependenciesInConfiguration)
      .collectInto(HashSet<LiveDependency>()) { set, dependencies -> set.addAll(dependencies) }
      .map { it as Set<LiveDependency> }

  private fun getDependenciesInConfiguration(configuration: Configuration): Set<LiveDependency> = configuration
      .fileCollection { it.version != "unspecified" }
      .filter { !it.canonicalPath.startsWith(projectPath) }
      .filter { File(it.path).isFile } // Removes erroneous directories that are sometimes used as dependency files
      .map { file -> LiveDependency(createDependencyInformation(file.path), file) }
      .toSet()

  private fun createDependencyInformation(path: String) = path
      .split(System.getProperty("file.separator"))
      .apply { if (size < 5) throw RuntimeException("Cannot create dependency information from \'$this\'.") }
      .let { it.subList(it.size - 5, it.size) }
      .map { it.toLowerCase() }
      .let { DependencyKey(group = it[0], name = it[1], version = it[2], filepath = it[4]) }
}