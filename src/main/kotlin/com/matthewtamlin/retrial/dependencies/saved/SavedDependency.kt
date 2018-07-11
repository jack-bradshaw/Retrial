package com.matthewtamlin.retrial.dependencies.saved

import com.matthewtamlin.retrial.hash.Sha512Hash
import com.matthewtamlin.retrial.dependencies.DependencyKey

/**
 * Historical representation of a dependency.
 */
data class SavedDependency(val key: DependencyKey, val hash: Sha512Hash)