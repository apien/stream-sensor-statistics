package com.apien.sss.integration
import pureconfig._

/**
  * @param parallelism Parallelism level of the application.
  */
case class Config(parallelism: Int)

object Config {
  import pureconfig.generic.auto._
  def load: Config = ConfigSource.default.loadOrThrow[Config]

}
