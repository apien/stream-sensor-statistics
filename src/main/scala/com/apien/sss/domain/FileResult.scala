package com.apien.sss.domain

import cats.Monoid

case class FileResult(stats: Map[SensorId, SensorStatistics]) {

  def update(sample: MeasurementSample)(implicit sensorStatisticsMonoid: Monoid[SensorStatistics]): FileResult = {
    val updatedStat = stats
      .get(sample.sensorId)
      .fold(sensorStatisticsMonoid.empty.put(sample))(currentStat => currentStat.put(sample))
    FileResult(stats.updated(sample.sensorId, updatedStat))
  }
}

object FileResult {
  lazy val init: FileResult = FileResult(Map())
}
