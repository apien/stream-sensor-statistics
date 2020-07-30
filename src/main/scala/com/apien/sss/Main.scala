package com.apien.sss

import cats.effect.ExitCode
import monix.eval.{Task, TaskApp}

object Main extends TaskApp {

  override def run(args: List[String]): Task[ExitCode] =
    for {
      _ <- ConsoleUi.run(args)
    } yield ExitCode.Success

}
