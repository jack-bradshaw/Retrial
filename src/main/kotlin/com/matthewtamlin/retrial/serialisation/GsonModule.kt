package com.matthewtamlin.retrial.serialisation

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GsonModule {
  @Provides
  @Singleton
  fun provideGson() = GsonBuilder().setPrettyPrinting().create()
}