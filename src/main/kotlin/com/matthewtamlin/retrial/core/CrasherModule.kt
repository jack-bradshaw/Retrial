package com.matthewtamlin.retrial.core

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CrasherModule {
  @Provides
  @Singleton
  fun provideCrasher() = Crasher()
}