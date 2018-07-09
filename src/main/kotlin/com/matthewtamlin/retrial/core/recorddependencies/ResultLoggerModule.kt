package com.matthewtamlin.retrial.core.recorddependencies

import dagger.Module
import dagger.Provides
import org.gradle.api.logging.Logger
import javax.inject.Singleton

@Module
class ResultLoggerModule {
  @Provides
  @Singleton
  fun provideResultLogger(logger: Logger): ResultLogger = GradleResultLogger(logger)
}