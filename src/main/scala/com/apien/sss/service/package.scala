package com.apien.sss

import cats.Show
import com.apien.sss.domain.ReportCalculator.FinalReport

package object service {

  implicit val reportShow: Show[FinalReport] = (report: FinalReport) => {
    val emptyToken = "NaN"
    val statsString = report.stats
      .map {
        case (sensor, stat) =>
          s"$sensor,${stat.min.getOrElse(emptyToken)},${stat.avg.getOrElse(emptyToken)},${stat.max.getOrElse(emptyToken)}"
      }
      .mkString("\n")

    s"""
       |Num of processed files: ${report.processedFiles}
       |Num of processed measurements: ${report.processedMeasurement}
       |Num of failed measurements: ${report.failedMeasurement}
       |
       |Sensors with highest avg humidity:
       |
       |sensor-id,min,avg,max
       |$statsString
        """.stripMargin
  }

}
