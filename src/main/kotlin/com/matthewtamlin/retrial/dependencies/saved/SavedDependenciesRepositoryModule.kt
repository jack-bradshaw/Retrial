package com.matthewtamlin.retrial.dependencies.saved

import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class SavedDependenciesRepositoryModule {
  @Provides
  @Singleton
  fun provideSavedDependenciesRepository(
      @DependencyDatabaseFile file: File,
      savedDependencySerialiser: SavedDependencySerialiser
  ): SavedDependenciesRepository {

    return FileBasedSavedDependenciesRepository(file, savedDependencySerialiser)
  }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DependencyDatabaseFile