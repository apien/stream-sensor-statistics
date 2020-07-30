package com.apien.sss.integration

import java.nio.file.{Path, Paths}

import ammonite.ops.ls
import com.apien.sss.domain.FileRepository
import monix.eval.Task
import os.{Path => AmonitePath}

import scala.util.Try

class LocalFileRepository extends FileRepository {

  //TODO Find files by io from fs2 package
  //TODO Maybe we need to show situation when such file?
  def findCsv(path: Path): Task[Option[List[Path]]] =
    Task {
      Try(ls.rec.apply((AmonitePath(path)))).toOption.map {
        _.filter(_.segments.toList.last.contains(".csv"))
          .map(p => Paths.get(p.toString()))
          .toList
      }
    }

}
