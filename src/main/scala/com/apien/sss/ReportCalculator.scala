package com.apien.sss

import cats.kernel.Monoid
import com.apien.sss.ReportCalculator.FinalReport
import com.apien.sss.domain.{SensorId, SensorStatistics}
import fs2.Stream
import monix.eval.Task

class ReportCalculator {

  def process(files: List[String])(implicit resultMonoid: Monoid[Map[SensorId, SensorStatistics]]): Task[FinalReport] =
    Stream(files: _*)
      .flatMap(MeasurementSamplesStream.process)
      .map(_.stats)
      .compile
      .foldMonoid
      .map { filesResult =>
        FinalReport(
          processedFiles = files.size,
          processedMeasurement = filesResult.map(_._2.samplesTotal).sum,
          failedMeasurement = filesResult.map(_._2.samplesInvalid).sum,
          stats = filesResult
        )
      }
}

object ReportCalculator {

  case class FinalReport(
      processedFiles: Int,
      processedMeasurement: Int,
      failedMeasurement: Int,
      stats: Map[SensorId, SensorStatistics]
  )

}
