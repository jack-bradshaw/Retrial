/*
 * Copyright 2018 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.retrial.hash

import io.reactivex.Single
import java.io.File
import java.nio.file.Files
import java.security.MessageDigest
import javax.inject.Provider

/**
 * Generates SHA2-512 hashes from files.
 */
class Sha512HashGenerator {
  private val sha512DigestProvider: Provider<MessageDigest> = Provider { MessageDigest.getInstance("SHA-512") }

  /**
   * Generates a SHA2-512 hash of the contents of a file.
   */
  fun generateHash(file: File) = Single.fromCallable {
    if (file.length() == 0L) {
      throw RuntimeException("Cannot calculate hash for empty file.")
    }

    sha512DigestProvider
        .get()
        .digest(Files.readAllBytes(file.toPath()))
        .map { toHexString(it) }
        .map { it.toUpperCase() }
        .joinToString(separator = "")
        .let { Sha512Hash(it) }
  }

  private fun toHexString(number: Byte) = String.format("%02x", number)
}