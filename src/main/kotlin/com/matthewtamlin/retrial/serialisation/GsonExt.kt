package com.matthewtamlin.retrial.serialisation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converts the supplied JSON structure to an instance of [T].
 */
inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)