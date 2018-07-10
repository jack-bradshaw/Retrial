package com.matthewtamlin.retrial.dependencies.saved

import com.google.gson.Gson
import com.matthewtamlin.retrial.serialisation.fromJson
import io.reactivex.Single

class SavedDependencySerialiser(private val gson: Gson) {
  fun serialise(dependency: Collection<SavedDependency>) = Single.fromCallable {
    gson.toJson(dependency)
  }

  fun deserialise(serialisation: String) = Single.fromCallable {
    gson.fromJson<Set<SavedDependency>>(serialisation)
  }
}