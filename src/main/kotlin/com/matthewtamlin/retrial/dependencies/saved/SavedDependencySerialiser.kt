package com.matthewtamlin.retrial.dependencies.saved

import com.google.gson.Gson
import com.matthewtamlin.retrial.serialisation.fromJson

class SavedDependencySerialiser(private val gson: Gson) {
  fun serialise(dependency: Collection<SavedDependency>): String {
    return gson.toJson(dependency)
  }

  fun deserialise(serialisation: String): Collection<SavedDependency> {
    return gson.fromJson(serialisation)
  }
}