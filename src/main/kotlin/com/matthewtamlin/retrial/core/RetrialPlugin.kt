package com.matthewtamlin.retrial.core

import io.reactivex.rxkotlin.subscribeBy
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
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