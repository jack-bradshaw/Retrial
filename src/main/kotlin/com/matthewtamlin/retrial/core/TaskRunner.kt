package com.matthewtamlin.retrial.core

import io.reactivex.Completable

interface TaskRunner {
  fun run(): Completable
}