package com.matthewtamlin.retrial.core

import io.reactivex.Completable

interface TaskRunner {
  fun createTask(): Completable
}