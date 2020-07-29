package com.apien.sss.domain

import cats.syntax.option._

/**
 * It represents statistics for the single sensor.
 *
 * @param samplesTotal   Total number of samples which occurred for the given sensor. It includes valid and invalid one.
 * @param samplesInvalid Total number of invalid samples.
 * @param humiditySum    Cumulated humidity from all valid measurements.
 * @param min            Minimum humidity.
 * @param max            Maximum humidity.
 */
case class SensorStatistics(
                             samplesTotal: Int,
                             samplesInvalid: Int,
                             humiditySum: Option[Long],
                             min: Option[Humidity],
                             max: Option[Humidity]
                           ) {

  /**
   * Number of valid samples.
   */
  lazy val samplesValid: Int = samplesTotal - samplesInvalid

  /**
   * Average humidity for the given sensor.
   */
  lazy val avg: Option[Double] = humiditySum.map(_ / samplesValid.toDouble)

  def put(measurementSample: MeasurementSample): SensorStatistics = {
    measurementSample.value match {
      case Measurement.InvalidMeasurement =>
        this.copy(samplesTotal = this.samplesTotal + 1, samplesInvalid = this.samplesInvalid + 1)
      case Measurement.ValidMeasurement(humidity) =>
        SensorStatistics(
          samplesTotal = this.samplesTotal + 1,
          samplesInvalid = this.samplesInvalid,
          humiditySum = (this.humiditySum.getOrElse(0L) + humidity.value.value).some,
          min = (humidity :: this.min.toList).minOption,
          max = (humidity :: this.max.toList).maxOption
        )
    }
  }
}

object SensorStatistics {
  lazy val init: SensorStatistics = SensorStatistics(
    samplesTotal = 0,
    samplesInvalid = 0,
    humiditySum = None,
    min = None,
    max = None
  )

}
