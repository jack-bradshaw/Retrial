package com.matthewtamlin.retrial.checksum

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChecksumGeneratorModule {
  @Provides
  @Singleton
  fun provideCompositeShaChecksumGenerator(): Sha512ChecksumGenerator = Sha512ChecksumGenerator()
}