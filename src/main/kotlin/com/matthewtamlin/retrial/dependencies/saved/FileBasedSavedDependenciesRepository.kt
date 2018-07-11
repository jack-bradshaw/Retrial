package com.matthewtamlin.retrial.dependencies.saved

import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * [SavedDependenciesRepository] that saves dependencies in a local file.
 */
class FileBasedSavedDependenciesRepository @Inject constructor(
    private val file: File,
    private val serialiser: SavedDependencySerialiser
) : SavedDependenciesRepository {

  override fun get(): Single<Set<SavedDependency>> = Single.defer {
    if (!file.exists()) {
      Single.just(HashSet())
    } else {
      serialiser.deserialise(file.readText())
    }
  }

  override fun set(dependencies: Set<SavedDependency>): Completable {
    return ensureFileExists()
        .andThen(serialiser.serialise(dependencies))
        .flatMapCompletable(::saveDependenciesToFile)
  }

  private fun ensureFileExists() = Completable.fromRunnable {
    if (!file.exists()) {
      file.createNewFile().also { success -> if (!success) throw IOException("Unable to create $file.") }
    }
  }

  private fun saveDependenciesToFile(serialisedDependencies: String) = Completable.fromRunnable {
    file.writeText(serialisedDependencies)
  }
}