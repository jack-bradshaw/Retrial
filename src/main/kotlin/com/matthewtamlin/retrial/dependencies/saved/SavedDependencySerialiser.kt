package com.matthewtamlin.retrial.dependencies.saved

import com.google.gson.Gson
import com.matthewtamlin.retrial.serialisation.fromJson
import io.reactivex.Single

/**
 * Serialises instances of [SavedDependency].
 */
class SavedDependencySerialiser(private val gson: Gson) {
  /**
   * Serialises a [SavedDependency] to a JSON string.
   */
  fun serialise(dependency: Collection<SavedDependency>) = Single.fromCallable {
    gson.toJson(dependency)
  }

  /**
   * Deserialises a [SavedDependency] from a JSON string
   */
  fun deserialise(serialisation: String) = Single.fromCallable {
    gson.fromJson<Set<SavedDependency>>(serialisation)
  }
}