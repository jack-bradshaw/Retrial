package com.matthewtamlin.retrial.dependencies.live

import com.matthewtamlin.retrial.dependencies.DependencyKey
import java.io.File

/**
 * Real-time representation of a dependency.
 */
data class LiveDependency(val key: DependencyKey, val file: File)