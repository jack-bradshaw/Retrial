package com.matthewtamlin.retrial.core.verifydependencies

import io.reactivex.Completable
import org.gradle.api.logging.Logger

class GradleResultLogger(private val logger: Logger): ResultLogger {
  override fun logSuccess() = Completable.fromRunnable { logger.info("Dependency verification passed.") }

  override fun logFailureDueTo(diff: DependencyDiff) = Completable.fromRunnable {
    StringBuilder()
        .appendln("Dependency verification failed.")
        .apply {
          if (!diff.additionalDependencies.isEmpty()) {
            appendln("\tAdditional dependencies:")

            diff.additionalDependencies.forEach { appendln("\t\t$it") }
          }

          if (!diff.missingDependencies.isEmpty()) {
            appendln("\tMissing dependencies:")

            diff.additionalDependencies.forEach { appendln("\t\t$it") }
          }

          if (!diff.additionalDependencies.isEmpty()) {
            appendln("\tChanged dependencies:")

            diff.additionalDependencies.forEach { appendln("\t\t$it") }
          }
        }
        .toString()
        .also { logger.error(it) }
  }
}