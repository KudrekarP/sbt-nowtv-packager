package com.nowtv.sbt.packager.rpm

import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerKeys._
import sbt._

trait ServiceSettings extends ConfigNameSettings {

  def serviceSettings: Seq[Def.Setting[_]] = Seq(
    linuxStartScriptTemplate := pluginResourceURL("systemv")
  )

  private def pluginResourceURL(resourceName: String) = {
    getClass.getClassLoader.getResource(s"templates/rpm/$resourceName")
  }

}
