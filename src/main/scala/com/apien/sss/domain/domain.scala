package com.apien.sss

import cats.syntax.either._
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.numeric._
import io.estatico.newtype.macros.newtype

import scala.util.Try

package object domain {

  type HumidityCondition = GreaterEqual[W.`0`.T] And LessEqual[W.`100`.T]
  type HumidityValue = Int Refined HumidityCondition

  @newtype case class SensorId(value: String)

  @newtype case class Humidity(value: HumidityValue)

  object Humidity {
    def parse(rawValue: String): Either[String, Humidity] =
      for {
        rawInt <- Try(rawValue.toInt).toEither.leftMap(_.getLocalizedMessage)
        value <- refineV[HumidityCondition](rawInt).map(v => Humidity.apply(v))
      } yield value

    implicit val ordering: HumidityOrdering = new HumidityOrdering()

    class HumidityOrdering(implicit intOrdering: Ordering[Int]) extends Ordering[Humidity] {
      override def compare(x: Humidity, y: Humidity): Int = intOrdering.compare(x.value.value, y.value.value)
    }

  }

}
