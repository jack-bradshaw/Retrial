package com.matthewtamlin.retrial.core

import io.reactivex.Completable

class Crasher {
  fun failBuild() = Completable.fromRunnable {
    throw RuntimeException()
  }
}