package com.nowtv.sbt.packager.rpm

import com.typesafe.sbt.SbtNativePackager._
import sbt.Keys._
import sbt._

trait RpmNameSettings extends ConfigNameSettings {

  val rpmNameTask = TaskKey[Option[String]]("get-rpm-name", "calculates the RPM name for the status file")

  def rpmNameSettings = Seq[Def.Setting[_]](
    rpmNameTask <<= (name in Rpm, nowtvRpmVersion, nowtvRpmRelease) map { (appName: String, version: Option[String], release: Option[String]) =>
      (version, release) match {
        case (Some(ver), Some(rel)) => Some(s"$appName-$ver-$rel.noarch.rpm")
        case (_, _) => None
      }
    }
  )

}
