package com.matthewtamlin.retrial.core

import com.matthewtamlin.retrial.checksum.ChecksumGeneratorModule
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepositoryModule
import com.matthewtamlin.retrial.dependencies.saved.DependencyDatabaseFile
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepositoryModule
import com.matthewtamlin.retrial.serialisation.GsonModule
import com.matthewtamlin.retrial.dependencies.saved.SavedDependencySerialiserModule
import dagger.BindsInstance
import dagger.Component
import org.gradle.api.Project
import java.io.File
import javax.inject.Singleton
import com.matthewtamlin.retrial.core.recorddependencies.ResultLoggerModule as RecordDependenciesResultLoggerModule
import com.matthewtamlin.retrial.core.recorddependencies.TaskRunnerModule as RecordDependenciesTaskRunnerModule
import com.matthewtamlin.retrial.core.verifydependencies.ResultLoggerModule as VerifyDependenciesResultLoggerModule
import com.matthewtamlin.retrial.core.verifydependencies.TaskRunnerModule as VerifyDependenciesTaskRunnerModule

@Component(
    modules = [
      ChecksumGeneratorModule::class,
      RecordDependenciesTaskRunnerModule::class,
      RecordDependenciesResultLoggerModule::class,
      VerifyDependenciesTaskRunnerModule::class,
      VerifyDependenciesResultLoggerModule::class,
      CrasherModule::class,
      LoggerModule::class, LiveDependenciesRepositoryModule::class,
      SavedDependenciesRepositoryModule::class,
      GsonModule::class,
      SavedDependencySerialiserModule::class])
@Singleton
interface CoreComponent {
  fun inject(plugin: RetrialPlugin)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun checksumsFile(@DependencyDatabaseFile file: File): Builder

    @BindsInstance
    fun project(project: Project): Builder

    fun build(): CoreComponent
  }
}