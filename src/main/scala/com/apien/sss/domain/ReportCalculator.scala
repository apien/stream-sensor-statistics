package com.apien.sss.domain

import cats.kernel.Monoid
import com.apien.sss.domain.ReportCalculator.FinalReport
import fs2.Stream
import monix.eval.Task

class ReportCalculator(parallelism: Int) {

  def process(files: List[String])(implicit resultMonoid: Monoid[Map[SensorId, SensorStatistics]]): Task[FinalReport] =
    Stream(
      Stream
        .evalSeq(Task(files))
        .flatMap(MeasurementSamplesStream.process)
    ).parJoin(parallelism)
      .map(_.stats)
      .compile
      .foldMonoid
      .map { filesResult =>
        FinalReport(
          processedFiles = files.size,
          processedMeasurement = filesResult.map(_._2.samplesTotal).sum,
          failedMeasurement = filesResult.map(_._2.samplesInvalid).sum,
          stats = filesResult.toList
            .sortBy(_._2.avg)
            .reverse
        )
      }
}

object ReportCalculator {

  case class FinalReport(
      processedFiles: Int,
      processedMeasurement: Int,
      failedMeasurement: Int,
      stats: List[(SensorId, SensorStatistics)]
  )

}
