package com.matthewtamlin.retrial.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import javax.inject.Inject
import com.matthewtamlin.retrial.core.recorddependencies.TaskRunner as RecordDependenciesTaskRunner
import com.matthewtamlin.retrial.core.verifydependencies.TaskRunner as VerifyDependenciesTaskRunner

open class RetrialPlugin : Plugin<Project> {
  private lateinit var project: Project

  @Inject
  lateinit var recordDependenciesTaskRunner: RecordDependenciesTaskRunner

  @Inject
  lateinit var verifyDependenciesTaskRunner: VerifyDependenciesTaskRunner

  @Inject
  lateinit var configuration: RetrialPluginConfiguration

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

    project.task("verifyDependencies")
  }

  private fun registerConfiguration() = project.extensions.add("retrial", RetrialPluginConfiguration::class.java)

  private fun getChecksumsFile() = configuration.checksumFile ?: project.file("./retrial-checksums.json")

  private fun registerVerifyDependenciesTask() {
    project.task("verifyDependencyChecksums").doLast {
      verifyDependenciesTaskRunner.createTask().subscribe()
    }
  }

  private fun registerRecordDependenciesTask() {
    project.task("recordDependencyChecksums").doLast {
      recordDependenciesTaskRunner.createTask().subscribe()
    }
  }
}