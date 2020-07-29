package com.apien.sss.service

import cats.implicits._
import com.apien.sss.domain.ReportCalculator
import monix.eval.Task

class Application(reportCalculator: ReportCalculator, out: String => Task[Unit]) {
  import com.apien.sss.domain.SensorStatistics.monoid
  def run(files: List[String]): Task[Unit] = {
    for {
      report <- reportCalculator.process(files)
      _<- out(report.show)
    } yield ()
  }
}
