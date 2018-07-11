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

import com.google.gson.Gson
import com.matthewtamlin.retrial.serialisation.fromJson
import io.reactivex.Single

/**
 * Serialises instances of [SavedDependency].
 */
class SavedDependencySerialiser(private val gson: Gson) {
  /**
   * Serialises a [SavedDependency] to a JSON string.
   */
  fun serialise(dependency: Collection<SavedDependency>) = Single.fromCallable {
    gson.toJson(dependency)
  }

  /**
   * Deserialises a [SavedDependency] from a JSON string
   */
  fun deserialise(serialisation: String) = Single.fromCallable {
    gson.fromJson<Set<SavedDependency>>(serialisation)
  }
}