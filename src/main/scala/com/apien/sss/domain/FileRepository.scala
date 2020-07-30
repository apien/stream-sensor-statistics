package com.apien.sss.domain

import java.nio.file.Path

import monix.eval.Task

trait FileRepository {

  /**
    * Find all files ended with '.csv'.
    *
    * It returns empty stream when no such file.
    *
    * @param path Directory.
    * @return Path to all csv files.
    */
  def findCsv(path: Path): Task[Option[List[Path]]]
}
