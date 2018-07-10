package com.matthewtamlin.retrial.core

import io.reactivex.Completable

class Crasher {
  fun failBuild() = Completable.fromRunnable {
    throw RuntimeException("Task execution failed. See logs for details.")
  }
}