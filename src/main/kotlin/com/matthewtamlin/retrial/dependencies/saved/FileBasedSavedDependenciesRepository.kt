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
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * [SavedDependenciesRepository] that saves dependencies in a local file.
 */
class FileBasedSavedDependenciesRepository @Inject constructor(
    private val file: File,
    private val serialiser: SavedDependencySerialiser
) : SavedDependenciesRepository {

  override fun get(): Single<Set<SavedDependency>> = Single.defer {
    if (!file.exists()) {
      Single.just(HashSet())
    } else {
      serialiser.deserialise(file.readText())
    }
  }

  override fun set(dependencies: Set<SavedDependency>): Completable {
    return ensureFileExists()
        .andThen(serialiser.serialise(dependencies))
        .flatMapCompletable(::saveDependenciesToFile)
  }

  private fun ensureFileExists() = Completable.fromRunnable {
    if (!file.exists()) {
      file.createNewFile().also { success -> if (!success) throw IOException("Unable to create $file.") }
    }
  }

  private fun saveDependenciesToFile(serialisedDependencies: String) = Completable.fromRunnable {
    file.writeText(serialisedDependencies)
  }
}