package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.dependencies.DependencyKey

data class DependencyDiff(
    val additionalDependencies: Set<DependencyKey>,
    val missingDependencies: Set<DependencyKey>,
    val changedDependencies: Set<DependencyKey>
) {
  val successful = additionalDependencies.isEmpty() && missingDependencies.isEmpty() && changedDependencies.isEmpty()
}