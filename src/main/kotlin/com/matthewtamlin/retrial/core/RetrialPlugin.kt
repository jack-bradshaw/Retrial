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

package com.matthewtamlin.retrial.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import javax.inject.Inject
import com.matthewtamlin.retrial.core.recorddependencies.TaskRunner as RecordDependenciesTaskRunner
import com.matthewtamlin.retrial.core.verifydependencies.TaskRunner as VerifyDependenciesTaskRunner

/**
 * Plugin for verifying dependency integrity at build time to prevent supply chain attacks.
 */
open class RetrialPlugin : Plugin<Project> {
  private lateinit var project: Project

  @Inject
  lateinit var recordDependenciesTaskRunner: RecordDependenciesTaskRunner

  @Inject
  lateinit var verifyDependenciesTaskRunner: VerifyDependenciesTaskRunner

  override fun apply(project: Project) {
    this.project = project

    registerConfiguration()

    DaggerCoreComponent
        .builder()
        .checksumsFile(getChecksumsFile())
        .project(project)
        .build()
        .inject(this)

    registerVerifyDependenciesTask()
    registerRecordDependenciesTask()
  }

  private fun registerConfiguration() = project.extensions.add("retrial", RetrialPluginConfiguration::class.java)

  private fun getChecksumsFile(): File {
    val configuration = project.extensions.getByType(RetrialPluginConfiguration::class.java)

    return configuration.checksumFile ?: project.file("./retrial-checksums.json")
  }

  private fun registerVerifyDependenciesTask() {
    project
        .task("verifyDependencyChecksums")
        .apply { group = "retrial" }
        .doLast {
          verifyDependenciesTaskRunner.run().blockingAwait()
        }
  }

  private fun registerRecordDependenciesTask() {
    project
        .task("recordDependencyChecksums")
        .apply { group = "retrial" }
        .doLast {
          recordDependenciesTaskRunner.run().blockingAwait()
        }
  }
}