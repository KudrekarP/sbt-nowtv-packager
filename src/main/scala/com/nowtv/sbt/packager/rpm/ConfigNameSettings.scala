package com.nowtv.sbt.packager.rpm

import sbt.Keys._
import sbt._

trait ConfigNameSettings extends VersionSettings {

  protected val nowtvRpmRelease = SettingKey[Option[String]]("nowtv-rpm-release", "NowTV Rpm Release")
  protected val nowtvRpmVersion = SettingKey[Option[String]]("nowtv-rpm-version", "NowTV Rpm Version")

  val nowtvRpmReleaseOverride = SettingKey[Option[String]]("nowtv-rpm-release-override", "NowTV Rpm Release")
  val nowtvRpmVersionOverride = SettingKey[Option[String]]("nowtv-rpm-version-override", "NowTV Rpm Version")

  def configNameSettings() = Seq[Def.Setting[_]](
    nowtvRpmRelease := {
      (nowtvRpmReleaseOverride.value, sys.env.get("APP_RELEASE")) match {
        case (Some(overridden), _) => Some(overridden)
        case (None, Some(env)) => Some(env)
        case (None, None) => Some(version.value)
      }
    },
    nowtvRpmReleaseOverride := None,
    nowtvRpmVersionOverride := None,
    nowtvRpmVersion := {
      (nowtvRpmVersionOverride.value, sys.env.get("APP_VERSION")) match {
        case (Some(overridden), _) => Some(overridden)
        case (None, Some(env)) => Some(env)
        case (None, None) => Some(version.value)
      }
    }
  )
}
