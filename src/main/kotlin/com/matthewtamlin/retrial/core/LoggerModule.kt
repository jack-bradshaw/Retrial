package com.matthewtamlin.retrial.core

import dagger.Module
import dagger.Provides
import org.gradle.api.Project
import javax.inject.Singleton

@Module
class LoggerModule {
  @Provides
  @Singleton
  fun provideLogger(project: Project) = project.logger
}