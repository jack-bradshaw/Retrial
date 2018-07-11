package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.dependencies.DependencyKey

data class DependencyDiff(
    val additionalDependencies: Set<DependencyKey> = setOf(),
    val missingDependencies: Set<DependencyKey> = setOf(),
    val changedDependencies: Set<DependencyKey> = setOf()
) {

  val empty = additionalDependencies.isEmpty() && missingDependencies.isEmpty() && changedDependencies.isEmpty()
}