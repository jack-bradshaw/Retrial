package com.matthewtamlin.retrial.core.recorddependencies

import io.reactivex.Completable
import org.gradle.api.logging.Logger

class GradleResultLogger(private val logger: Logger) : ResultLogger {
  override fun logSuccess() = Completable.fromRunnable { logger.info("Dependency checksums recorded successful.") }

  override fun logFailure() = Completable.fromRunnable { logger.error("Failed to record dependency checksums.") }
}