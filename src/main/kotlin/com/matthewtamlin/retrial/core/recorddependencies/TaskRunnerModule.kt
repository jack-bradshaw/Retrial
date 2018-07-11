package com.matthewtamlin.retrial.core.recorddependencies

import com.matthewtamlin.retrial.hash.Sha512HashGenerator
import com.matthewtamlin.retrial.core.Crasher
import com.matthewtamlin.retrial.dependencies.live.ProjectBasedLiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TaskRunnerModule {
  @Provides
  @Singleton
  fun provideTaskRunner(
      savedDependenciesRepository: SavedDependenciesRepository,
      liveDependenciesRepository: ProjectBasedLiveDependenciesRepository,
      hashGenerator: Sha512HashGenerator,
      crasher: Crasher
  ) = TaskRunner(savedDependenciesRepository, liveDependenciesRepository, hashGenerator)
}