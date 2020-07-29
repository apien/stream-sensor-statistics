package com.apien.sss.test

import com.apien.sss.domain.{Humidity, SensorId}
import eu.timepit.refined.api.Refined
import monix.execution.Scheduler
import org.scalatest.flatspec
import org.scalatest.matchers.should

trait SensorSpec extends flatspec.AnyFlatSpec with should.Matchers {

  protected implicit val scheduler: Scheduler = Scheduler.global

  implicit def intToMeasurement(value: Int): Humidity = Humidity(Refined.unsafeApply(value))

  implicit def stringToSensorId(value: String): SensorId = SensorId(value)

}
