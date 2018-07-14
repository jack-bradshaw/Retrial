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

import io.github.glytching.junit.extension.folder.TemporaryFolder
import io.github.glytching.junit.extension.folder.TemporaryFolderExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(TemporaryFolderExtension::class)
class Sha512HashGeneratorTests {

  private lateinit var file: File

  private lateinit var generator: Sha512HashGenerator

  @BeforeEach
  fun setup(temporaryFolder: TemporaryFolder) {
    file = temporaryFolder.createFile("temporary file")
    generator = Sha512HashGenerator()
  }

  @Nested
  @DisplayName("generateHash(File)")
  inner class TestGenerateHash {

    @Test
    @DisplayName("should return the SHA2-512 hash of an empty string when the file is empty")
    fun fileIsEmpty() {
      generator
          .generateHash(file)
          .test()
          .assertResult(Sha512Hash("E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855"))
    }

    @Test
    @DisplayName("should return the SHA2-512 hash of the file contents when the file is not empty")
    fun fileIsNotEmpty() {
      val input = "A"

      val checksum =
          Sha512Hash("21B4F4BD9E64ED355C3EB676A28EBEDAF6D8F17BDC365995B319097153044080516BD083BFCCE66121A3072646994C8430CC382B8DC543E84880183BF856CFF5")

      file.writeText(input)

      generator
          .generateHash(file)
          .test()
          .assertResult(checksum)
    }
  }
}