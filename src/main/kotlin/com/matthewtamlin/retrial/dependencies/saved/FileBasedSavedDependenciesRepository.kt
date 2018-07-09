package com.matthewtamlin.retrial.dependencies.saved

import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FileBasedSavedDependenciesRepository @Inject constructor(
    private val file: File,
    private val serialiser: SavedDependencySerialiser
) : SavedDependenciesRepository {

  override fun get(): Single<Set<SavedDependency>> = Single.fromCallable {
    if (!file.exists()) {
      HashSet<SavedDependency>()
    } else {
      HashSet(serialiser.deserialise(file.readText()))
    }
  }

  override fun set(dependencies: Set<SavedDependency>): Completable = Completable.fromRunnable {
    if (!file.exists()) {
      setupFile()
    }

    file.writeText(serialiser.serialise(dependencies))
  }

  private fun setupFile() {
    file.createNewFile().also { success -> if (!success) throw IOException("Unable to create $file.") }
  }
}