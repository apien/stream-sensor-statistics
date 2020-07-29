package com.apien.sss

import cats.kernel.Monoid
import cats.syntax.option._
import com.apien.sss.domain.ReportCalculator.FinalReport
import com.apien.sss.domain.{ReportCalculator, SensorId, SensorStatistics}
import com.apien.sss.test.{ResourceSpec, SensorSpec}

class ReportCalculatorSpec extends SensorSpec with ResourceSpec {

  import SensorStatistics.monoid
  import cats.instances.map._

  val report = new ReportCalculator

  "ReportCalculator.process" should "provide data" in {
    report.process(List(getPathToResource("/simple.csv"))).runSyncUnsafe() shouldBe FinalReport(
      processedFiles = 1,
      processedMeasurement = 6,
      failedMeasurement = 2,
      stats = Map(
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
  }

  it should "aggregate data from multiple files" in {
    import cats.implicits._
    val files = List(
      getPathToResource("/simple.csv"),
      getPathToResource("/simple2.csv")
    )
    val maybeInt = Monoid[Option[Int]].combine(1.some, None)
    maybeInt shouldBe 1.some

    report.process(files).runSyncUnsafe() shouldBe FinalReport(
      processedFiles = 2,
      processedMeasurement = 13,
      failedMeasurement = 5,
      stats = Map(
        SensorId("s1") -> SensorStatistics(
          samplesTotal = 5,
          samplesInvalid = 1,
          humiditySum = 230L.some,
          min = intToMeasurement(12).some,
          max = intToMeasurement(100).some
        ),
        SensorId("s2") -> SensorStatistics(
          samplesTotal = 3,
          samplesInvalid = 3,
          humiditySum = None,
          min = None,
          max = None
        ),
        SensorId("s3") -> SensorStatistics(
          samplesTotal = 2,
          samplesInvalid = 0,
          humiditySum = 74L.some,
          min = intToMeasurement(21).some,
          max = intToMeasurement(53).some
        ),
        SensorId("s4") -> SensorStatistics(
          samplesTotal = 1,
          samplesInvalid = 0,
          humiditySum = 99L.some,
          min = intToMeasurement(99).some,
          max = intToMeasurement(99).some
        ),
        SensorId("s6") -> SensorStatistics(
          samplesTotal = 1,
          samplesInvalid = 0,
          humiditySum = 87L.some,
          min = intToMeasurement(87).some,
          max = intToMeasurement(87).some
        ),
        SensorId("s7") -> SensorStatistics(
          samplesTotal = 1,
          samplesInvalid = 1,
          humiditySum = None,
          min = None,
          max = None
        )
      )
    )
  }
}
