package com.apien.sss.domain

import cats.syntax.option._
import com.apien.sss.test.{ResourceSpec, SensorSpec}

class MeasurementSamplesStreamSpec extends SensorSpec with ResourceSpec {

  "MeasurementSampleStream" should "read valid values" in {
    MeasurementSamplesStream.load(getPathToResource("/all_valid.csv")).compile.toList.runSyncUnsafe() shouldBe List(
      MeasurementSample("s1", Measurement.ValidMeasurement(10)),
      MeasurementSample("s2", Measurement.ValidMeasurement(88)),
      MeasurementSample("s1", Measurement.ValidMeasurement(53))
    )
  }

  it should "read values which can not be parsed on valid samples (out of range or value is not Integer)" in {
    MeasurementSamplesStream.load(getPathToResource("/invalid.csv")).compile.toList.runSyncUnsafe() shouldBe List(
      MeasurementSample("s1", Measurement.InvalidMeasurement),
      MeasurementSample("s1", Measurement.ValidMeasurement(97)),
      MeasurementSample("s2", Measurement.InvalidMeasurement),
      MeasurementSample("s5", Measurement.ValidMeasurement(2))
    )
  }

  it should "read NaN" in {
    MeasurementSamplesStream.load(getPathToResource("/simple.csv")).compile.toList.runSyncUnsafe() shouldBe List(
      MeasurementSample("s1", Measurement.ValidMeasurement(34)),
      MeasurementSample("s2", Measurement.InvalidMeasurement),
      MeasurementSample("s3", Measurement.ValidMeasurement(53)),
      MeasurementSample("s1", Measurement.InvalidMeasurement),
      MeasurementSample("s1", Measurement.ValidMeasurement(84)),
      MeasurementSample("s1", Measurement.ValidMeasurement(12))
    )
  }

  it should "trim values" in {
    MeasurementSamplesStream.load(getPathToResource("/require_trim.csv")).compile.toList.runSyncUnsafe() shouldBe List(
      MeasurementSample("s1", Measurement.ValidMeasurement(34)),
      MeasurementSample("s1", Measurement.ValidMeasurement(76)),
      MeasurementSample("s5", Measurement.ValidMeasurement(4))
    )
  }

  "MeasurementSamplesStream.process" should "calculate statistics for the file" in {
    MeasurementSamplesStream
      .process(getPathToResource("/simple.csv"))
      .compile
      .toList
      .runSyncUnsafe() shouldBe List(
      FileResult(
        Map(
          SensorId("s1") -> SensorStatistics(
            samplesTotal = 4,
            samplesInvalid = 1,
            humiditySum = 130L.some,
            min = intToMeasurement(12).some,
            max = intToMeasurement(84).some
          ),
          SensorId("s2") -> SensorStatistics(
            samplesTotal = 1,
            samplesInvalid = 1,
            humiditySum = None,
            min = None,
            max = None
          ),
          SensorId("s3") -> SensorStatistics(
            samplesTotal = 1,
            samplesInvalid = 0,
            humiditySum = 53L.some,
            min = intToMeasurement(53).some,
            max = intToMeasurement(53).some
          )
        )
      )
    )
  }

}
