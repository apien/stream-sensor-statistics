package com.apien.sss.domain

case class FileResult(stats: Map[SensorId, SensorStatistics]) {

  def update(sample: MeasurementSample): FileResult = {
    val updatedStat = stats
      .get(sample.sensorId)
      .fold(SensorStatistics.init.put(sample))(currentStat => currentStat.put(sample))
    FileResult(stats.updated(sample.sensorId, updatedStat))
  }
}

object FileResult {
  lazy val init: FileResult = FileResult(Map())
}
