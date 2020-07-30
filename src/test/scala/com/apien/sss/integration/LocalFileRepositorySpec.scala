package com.apien.sss.integration

import java.nio.file.Paths
import java.util.UUID

import com.apien.sss.test.{ResourceSpec, SensorSpec}

class LocalFileRepositorySpec extends SensorSpec with ResourceSpec {
  private val repository = new LocalFileRepository

  "FileUtil.findCsv" should "find all csv files even nested" in {
    repository
      .findCsv(Paths.get(getPathToResource("/")))
      .runSyncUnsafe()
      .get
      .map(_.getFileName.toString) shouldBe
      List("all_valid.csv", "invalid.csv", "simple.csv", "simple2.csv", "nested.csv")
  }

  it should "return empty Stream when the indicated files do not contain csv files" in {
    repository
      .findCsv(Paths.get(getPathToResource("/txt")))
      .runSyncUnsafe()
      .get
      .map(_.getFileName.toString) shouldBe Nil
  }

  it should "no such directory" in {
    repository
      .findCsv(Paths.get(s"/${UUID.randomUUID().toString}"))
      .runSyncUnsafe() shouldBe None
  }
}