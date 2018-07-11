package com.matthewtamlin.retrial.checksum

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class Sha512ChecksumGeneratorModule {
  @Provides
  @Singleton
  fun provideCompositeShaChecksumGenerator(): Sha512ChecksumGenerator = Sha512ChecksumGenerator()
}