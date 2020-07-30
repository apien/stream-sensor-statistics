package com.apien.sss.domain

import java.nio.file.Paths

import cats.effect.Blocker
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
          .map(_.split(",").toList)
          .map(_.map(_.trim))
          .map(MeasurementSample.parse)
          .collect { case Some(measurement) => measurement }
      }

  def process(filePath: String): Stream[Task, FileResult] =
    load(filePath)
      .fold(FileResult.init) { case (acc, measurementSample) => acc.update(measurementSample) }
}
