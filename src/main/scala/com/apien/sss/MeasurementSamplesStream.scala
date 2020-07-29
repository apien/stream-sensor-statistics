package com.apien.sss

import java.nio.file.Paths

import cats.effect.Blocker
import com.apien.sss.domain.{FileResult, MeasurementSample}
import fs2.{Stream, io, text}
import monix.eval.Task

object MeasurementSamplesStream {

  def load(filePath: String): Stream[Task, MeasurementSample] =
    Stream
      .resource(Blocker[Task])
      .flatMap { blocker =>
        io.file
          .readAll[Task](Paths.get(filePath), blocker, 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .drop(1)
          .map(v => MeasurementSample.parse(v.split(",").toList))
          .collect { case Some(measurement) => measurement }
      }

  def process(filePath: String): Stream[Task, FileResult] =
    load(filePath)
      .fold(FileResult.init) { case (acc, measurementSample) => acc.update(measurementSample) }
}
