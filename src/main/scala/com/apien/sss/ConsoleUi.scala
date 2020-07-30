package com.apien.sss

import java.nio.file.Paths

import com.apien.sss.domain.ReportCalculator
import com.apien.sss.integration.{Config, LocalFileRepository}
import com.apien.sss.service.Application
import monix.eval.Task

object ConsoleUi {

  private val stdOut: String => Task[Unit] = str => Task(println(str))

  def run(args: List[String]): Task[Unit] =
    for {
      config <- Task(Config.load)
      dir <- Task(args.headOption)
      _ <- dir match {
        case Some(path) =>
          val app = new Application(
            new LocalFileRepository,
            new ReportCalculator(config.parallelism),
            stdOut
          )
          app.run(Paths.get(path))
        case None =>
          stdOut("Application requires single argument with directory path!")
      }
    } yield ()
}
