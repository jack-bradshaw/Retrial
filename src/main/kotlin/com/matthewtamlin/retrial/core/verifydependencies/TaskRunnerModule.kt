package com.matthewtamlin.retrial.core.verifydependencies

import com.matthewtamlin.retrial.checksum.Sha512ChecksumGenerator
import com.matthewtamlin.retrial.core.Crasher
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
      checksumGenerator: Sha512ChecksumGenerator,
      resultLogger: ResultLogger
  ) = TaskRunner(savedDependenciesRepository, liveDependenciesRepository, checksumGenerator, resultLogger)
}