package com.matthewtamlin.retrial.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import javax.inject.Inject
import com.matthewtamlin.retrial.core.recorddependencies.TaskRunner as RecordDependenciesTaskRunner
import com.matthewtamlin.retrial.core.verifydependencies.TaskRunner as VerifyDependenciesTaskRunner

open class RetrialPlugin : Plugin<Project> {
  private lateinit var project: Project

  @Inject
  lateinit var recordDependenciesTaskRunner: RecordDependenciesTaskRunner

  @Inject
  lateinit var verifyDependenciesTaskRunner: VerifyDependenciesTaskRunner

  override fun apply(project: Project) {
    this.project = project

    project.extensions.add("retrial", RetrialPluginConfiguration::class.java)

    DaggerCoreComponent
        .builder()
        .checksumsFile(getChecksumsFile())
        .project(project)
        .build()
        .inject(this)

    exposeVerifyDependenciesTask()
    exposeRecordDependenciesTask()

    project.task("verifyDependencies")
  }

  private fun getChecksumsFile(): File {
    val configuration = project.extensions.getByType(RetrialPluginConfiguration::class.java)

    return configuration.checksumFile ?: project.file("./retrial-checksums.json")
  }

  private fun exposeVerifyDependenciesTask() {
    project.task("verifyDependencyChecksums").doLast {
      verifyDependenciesTaskRunner.createTask().subscribe()
    }
  }

  private fun exposeRecordDependenciesTask() {
    project.task("recordDependencyChecksums").doLast {
      recordDependenciesTaskRunner.createTask().subscribe()
    }
  }
}