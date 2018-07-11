package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.hash.Sha512HashGenerator
import dagger.Module
import dagger.Provides
import com.matthewtamlin.retrial.dependencies.live.ProjectBasedLiveDependenciesRepository
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepository
import javax.inject.Singleton

@Module
class TaskRunnerModule {
  @Provides
  @Singleton
  fun provideTaskRunner(
      savedDependenciesRepository: SavedDependenciesRepository,
      liveDependenciesRepository: ProjectBasedLiveDependenciesRepository,
      hashGenerator: Sha512HashGenerator,
      resultLogger: ResultLogger
  ) = TaskRunner(savedDependenciesRepository, liveDependenciesRepository, hashGenerator, resultLogger)
}