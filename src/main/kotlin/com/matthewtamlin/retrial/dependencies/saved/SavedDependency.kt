package com.matthewtamlin.retrial.dependencies.saved

import com.matthewtamlin.retrial.hash.Sha512Checksum
import com.matthewtamlin.retrial.dependencies.DependencyKey

data class SavedDependency(val key: DependencyKey, val checksum: Sha512Checksum)