package com.apien.sss.domain

import com.apien.sss.domain.domain.Humidity

sealed trait Measurement

object Measurement {

  case object InvalidMeasurement extends Measurement

  case class ValidMeasurement(humidity: Humidity) extends Measurement

}
