package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.dependencies.DependencyKey

/**
 * The difference between the saved dependencies and the live dependencies of a project.
 *
 * @property additionalDependencies
 * the dependencies that are present in the live dependencies but not the saved dependencies
 *
 * @property missingDependencies
 * the dependencies that are present in the saved dependencies but not the live dependencies
 *
 * @property changedDependencies
 * the dependencies that are present in both the live dependencies and the saved dependencies, but with different
 * content
 */
data class DependencyDiff(
    val additionalDependencies: Set<DependencyKey> = setOf(),
    val missingDependencies: Set<DependencyKey> = setOf(),
    val changedDependencies: Set<DependencyKey> = setOf()
) {

  /**
   * True if there are no additional dependencies, no missing dependencies and no changed dependencies, false otherwise.
   */
  val empty = additionalDependencies.isEmpty() && missingDependencies.isEmpty() && changedDependencies.isEmpty()
}