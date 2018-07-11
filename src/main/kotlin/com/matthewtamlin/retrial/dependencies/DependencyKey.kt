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