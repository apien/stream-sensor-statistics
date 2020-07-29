package com.apien.sss.domain

import cats.syntax.option._

case class MeasurementSample(sensorId: SensorId, value: Measurement)

object MeasurementSample {

  /**
   * Parse list of string to [[MeasurementSample]].
   *
   * When list does not contain a right number of values then return [[None]].
   * When list contains two elements but humidity is not valid value then return [[MeasurementSample]] with [[InvalidMeasurement]].
   *
   * @param values List of values to parse. It should contain two elements in the following order: sensor id, humidity value.
   * @return
   */
  def parse(values: List[String]): Option[MeasurementSample] =
    values match {
      case id :: value :: Nil =>
        Humidity
          .parse(value)
          .fold(
            _ => MeasurementSample(SensorId(id), Measurement.InvalidMeasurement).some,
            humidity => MeasurementSample(SensorId(id), Measurement.ValidMeasurement(humidity)).some
          )

      case _ => None
    }
}
