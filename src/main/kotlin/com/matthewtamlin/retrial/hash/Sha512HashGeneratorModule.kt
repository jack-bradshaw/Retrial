package com.matthewtamlin.retrial.hash

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class Sha512HashGeneratorModule {
  @Provides
  @Singleton
  fun provideCompositeShaChecksumGenerator(): Sha512HashGenerator = Sha512HashGenerator()
}