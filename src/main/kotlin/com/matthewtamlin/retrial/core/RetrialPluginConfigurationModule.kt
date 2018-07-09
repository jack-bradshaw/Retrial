package com.matthewtamlin.retrial.core

import dagger.Module
import dagger.Provides
import org.gradle.api.Project
import javax.inject.Singleton

@Module
class RetrialPluginConfigurationModule {
  @Provides
  @Singleton
  fun providesRetrialPluginConfiguration(project: Project): RetrialPluginConfiguration {
    return project.extensions.getByType(RetrialPluginConfiguration::class.java)
  }
}