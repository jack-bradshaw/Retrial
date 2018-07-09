package com.matthewtamlin.retrial.dependencies

data class DependencyKey(
    val group: String,
    val name: String,
    val version: String,
    val filepath: String
)