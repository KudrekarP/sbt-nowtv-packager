package com.nowtv.sbt.packager.rpm

import sbt._

object SbtNowtvPackager extends Plugin with RpmPackagingSettings with ConfigNameSettings with RpmNameSettings with StatusPropertiesSettings with ServiceSettings {

  def nowtvRpmSettings: Seq[Def.Setting[_]] = {
    configNameSettings ++
    rpmNameSettings ++
    statusPropertiesSettings ++
    rpmPackagingSettings ++
    serviceSettings
  }
}