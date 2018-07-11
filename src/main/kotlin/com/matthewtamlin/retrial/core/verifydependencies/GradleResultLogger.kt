package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.dependencies.DependencyKey
import io.reactivex.Completable
import org.gradle.api.logging.Logger

/**
 * A [ResultLogger] that formats logs and delegates to a [Logger].
 */
class GradleResultLogger(private val logger: Logger) : ResultLogger {
  override fun logSuccess() = Completable.fromRunnable { logger.info("Dependency verification passed.") }

  override fun logFailureDueTo(diff: DependencyDiff) = Completable.fromRunnable {
    StringBuilder()
        .appendln("Dependency verification failed.")
        .apply {
          if (!diff.additionalDependencies.isEmpty()) {
            appendln("\tAdditional dependencies:")

            diff.additionalDependencies.forEach { appendln("\t\t${prettyFormatDependencyKey(it)}") }
          }

          if (!diff.missingDependencies.isEmpty()) {
            appendln("\tMissing dependencies:")

            diff.missingDependencies.forEach { appendln("\t\t${prettyFormatDependencyKey(it)}") }
          }

          if (!diff.changedDependencies.isEmpty()) {
            appendln("\tChanged dependencies:")

            diff.changedDependencies.forEach { appendln("\t\t${prettyFormatDependencyKey(it)}") }
          }
        }
        .toString()
        .also { logger.error(it) }
  }

  private fun prettyFormatDependencyKey(key: DependencyKey):String {
    return "group: ${key.group}, name: ${key.name}, version: ${key.version}"
  }
}