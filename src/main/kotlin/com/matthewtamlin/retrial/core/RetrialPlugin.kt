package com.matthewtamlin.retrial.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import javax.inject.Inject
import com.matthewtamlin.retrial.core.recorddependencies.TaskRunner as RecordDependenciesTaskRunner
import com.matthewtamlin.retrial.core.verifydependencies.TaskRunner as VerifyDependenciesTaskRunner

open class RetrialPlugin : Plugin<Project> {
  private lateinit var project: Project

  private lateinit var configuration: RetrialPluginConfiguration

  @Inject
  lateinit var recordDependenciesTaskRunner: RecordDependenciesTaskRunner

  @Inject
  lateinit var verifyDependenciesTaskRunner: VerifyDependenciesTaskRunner

  override fun apply(project: Project) {
    this.project = project

    registerConfiguration()

    configuration = project.extensions.getByType(RetrialPluginConfiguration::class.java)

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

  private fun getChecksumsFile() = configuration.checksumFile ?: project.file("./retrial-checksums.json")

  private fun registerVerifyDependenciesTask() {
    project
        .task("verifyDependencyChecksums")
        .apply { group = "retrial" }
        .doLast {
          verifyDependenciesTaskRunner.run().subscribe()
        }
  }

  private fun registerRecordDependenciesTask() {
    project
        .task("recordDependencyChecksums")
        .apply { group = "retrial" }
        .doLast {
          recordDependenciesTaskRunner.run().subscribe()
        }
  }
}