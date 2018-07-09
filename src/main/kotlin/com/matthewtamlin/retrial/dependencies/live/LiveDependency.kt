package com.matthewtamlin.retrial.dependencies.live

import com.matthewtamlin.retrial.dependencies.DependencyKey
import java.io.File

data class LiveDependency(val key: DependencyKey, val file: File)