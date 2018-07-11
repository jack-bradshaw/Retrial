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

import com.matthewtamlin.retrial.hash.Sha512HashGeneratorModule
import com.matthewtamlin.retrial.dependencies.live.LiveDependenciesRepositoryModule
import com.matthewtamlin.retrial.dependencies.saved.DependencyDatabaseFile
import com.matthewtamlin.retrial.dependencies.saved.SavedDependenciesRepositoryModule
import com.matthewtamlin.retrial.dependencies.saved.SavedDependencySerialiserModule
import com.matthewtamlin.retrial.serialisation.GsonModule
import dagger.BindsInstance
import dagger.Component
import org.gradle.api.Project
import java.io.File
import javax.inject.Singleton
import com.matthewtamlin.retrial.core.recorddependencies.TaskRunnerModule as RecordDependenciesTaskRunnerModule
import com.matthewtamlin.retrial.core.verifydependencies.ResultLoggerModule as VerifyDependenciesResultLoggerModule
import com.matthewtamlin.retrial.core.verifydependencies.TaskRunnerModule as VerifyDependenciesTaskRunnerModule

@Component(
    modules = [
      Sha512HashGeneratorModule::class,
      RecordDependenciesTaskRunnerModule::class,
      VerifyDependenciesTaskRunnerModule::class,
      VerifyDependenciesResultLoggerModule::class,
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