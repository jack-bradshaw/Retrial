package com.matthewtamlin.retrial.core

import io.reactivex.Completable
import org.gradle.api.tasks.TaskExecutionException

class Crasher {
  fun failBuild() = Completable.fromRunnable {
    throw RuntimeException()
  }
}