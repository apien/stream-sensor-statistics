package com.apien.sss.domain

sealed trait Measurement

object Measurement {

  case object InvalidMeasurement extends Measurement

  case class ValidMeasurement(humidity: Humidity) extends Measurement

}
