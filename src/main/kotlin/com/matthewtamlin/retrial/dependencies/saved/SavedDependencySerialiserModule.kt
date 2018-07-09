package com.matthewtamlin.retrial.dependencies.saved

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SavedDependencySerialiserModule {
  @Provides
  @Singleton
  fun provideSavedDependencySerialiserModule(gson: Gson) = SavedDependencySerialiser(
      gson)
}