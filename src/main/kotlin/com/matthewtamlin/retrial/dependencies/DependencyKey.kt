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

package com.matthewtamlin.retrial.dependencies

/**
 * Unique identifier for a dependency.
 *
 * @property group the group ID of the dependency
 * @property name the name of the dependency
 * @property version version or version interval or the dependency
 * @property filepath the relative ath to the file artifact
 */
data class DependencyKey(
    val group: String,
    val name: String,
    val version: String,
    val filepath: String
)