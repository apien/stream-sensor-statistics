package com.apien.sss

import java.nio.file.Paths

import com.apien.sss.domain.ReportCalculator
import com.apien.sss.integration.LocalFileRepository
import com.apien.sss.service.Application
import monix.eval.Task

object ConsoleUi {

  private val stdOut: String => Task[Unit] = str => Task(println(str))

  def run(args: List[String]): Task[Unit] =
    args.headOption match {
      case Some(path) =>
        val app = new Application(
          new LocalFileRepository,
          new ReportCalculator(),
          stdOut
        )
        app.run(Paths.get(path))
      case None =>
        stdOut("Application requires single argument with directory path!")
    }
}
