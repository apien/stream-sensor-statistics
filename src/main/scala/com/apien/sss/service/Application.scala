package com.apien.sss.service

import java.nio.file.Path

import cats.implicits._
import com.apien.sss.domain.{FileRepository, ReportCalculator}
import monix.eval.Task

/**
  * Main flow of a application which:
  * - find csv files in the given directory
  * - calculate statistics for sensors
  * - display report for all sensors
  * @param fileRepository File repository.
  * @param reportCalculator Conduct a statistics calculations.
  * @param out Put message on some output.
  */
class Application(
    fileRepository: FileRepository,
    reportCalculator: ReportCalculator,
    out: String => Task[Unit]
) {
  import com.apien.sss.domain.SensorStatistics.monoid
  def run(directoryPath: Path): Task[Unit] = {
    fileRepository
      .findCsv(directoryPath)
      .flatMap {
        _.fold(out(s"There is issue to access a directory `$directoryPath`. Possible issues: access denied or directory does not exist.")) {
          csvFiles =>
            for {
              report <- reportCalculator.process(csvFiles.map(_.toString))
              _ <- out(report.show)
            } yield ()
        }
      }
  }
}
