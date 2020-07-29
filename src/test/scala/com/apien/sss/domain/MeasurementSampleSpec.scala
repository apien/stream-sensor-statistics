package com.apien.sss.domain

import cats.syntax.option._
import com.apien.sss.domain.Measurement.InvalidMeasurement
import com.apien.sss.test.SensorSpec

class MeasurementSampleSpec extends SensorSpec {

  "MeasurementSample.parse" should "return parsed object" in {
    MeasurementSample.parse(List("s1", "45")) shouldBe MeasurementSample("s1", Measurement.ValidMeasurement(45)).some
  }

  it should "return None when list size is less than 2" in {
    MeasurementSample.parse(List("s1")) shouldBe None
  }

  it should "return InvalidMeasurement when humidity value is not valid Integer number" in {
    MeasurementSample.parse(List("s1", "xyz")) shouldBe MeasurementSample("s1", InvalidMeasurement).some
  }

  it should "return InvalidMeasurement when humidity value is above 100" in {
    MeasurementSample.parse(List("s1", "101")) shouldBe MeasurementSample("s1", InvalidMeasurement).some
  }

  it should "return InvalidMeasurement when humidity value is below 0" in {
    MeasurementSample.parse(List("s1", "-1")) shouldBe MeasurementSample("s1", InvalidMeasurement).some
  }

  it should "return InvalidMeasurement when humidity equals `NaN`" in {
    MeasurementSample.parse(List("s1", "NaN")) shouldBe MeasurementSample("s1", InvalidMeasurement).some
  }

}
