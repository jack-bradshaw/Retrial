package com.matthewtamlin.retrial.core

import java.io.File

/**
 * Provides configuration control for the Retrial plugin.
 */
open class RetrialPluginConfiguration {
  /**
   * Overrides the file to use to store dependency checksums.
   */
  var checksumFile: File? = null
}