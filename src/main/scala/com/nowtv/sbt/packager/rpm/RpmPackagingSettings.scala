package com.nowtv.sbt.packager.rpm

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerKeys._
import com.typesafe.sbt.packager.archetypes.TemplateWriter
import sbt.Keys._
import sbt._

trait RpmPackagingSettings extends ConfigNameSettings {

  def rpmPackagingSettings: Seq[Def.Setting[_]] = Seq(
    //platform in Rpm
    name in Rpm := s"${if(name.value.startsWith("nowtv-")){name.value}else{"popcorn-"+name.value}}",
    packageName := s"${if(name.value.startsWith("nowtv-")){name.value}else{"popcorn-"+name.value}}",
    version in Rpm := nowtvRpmVersion.value.getOrElse("0.0"),
    rpmRelease := nowtvRpmRelease.value.getOrElse("0.1"),
    rpmBrpJavaRepackJars := true,
    packageSummary in Rpm := s"${name.value} app",
    packageDescription in Rpm <<= name,
    maintainer in Rpm := "NowTV Platform Team",
    rpmVendor := "NowTV",
    rpmLicense := Some("BSkyB"),
    rpmGroup := Some("NowTV Platform Team"),

    linuxPackageSymlinks := Seq.empty,
    defaultLinuxInstallLocation := "/var/sky",
    defaultLinuxLogsLocation := "/var/sky",

    daemonUser in Linux := "tomcat",
    daemonGroup in Linux := "tomcat",
    bashScriptEnvConfigLocation := None, // don't need this as we are setting the env var in our own systemv script

    rpmPre := Some(loadPluginResource("preinst", (linuxScriptReplacements in Rpm).value)),
    rpmPost := Some(loadPluginResource("postinst", (linuxScriptReplacements in Rpm).value)),
    rpmPreun := Some(loadPluginResource("preun", (linuxScriptReplacements in Rpm).value)),
    rpmPostun := Some(loadPluginResource("postun", (linuxScriptReplacements in Rpm).value)),

    linuxScriptReplacements += {
      "loader-functions" -> loadPluginResource("custom_loader_functions", Nil)
    },

    // remove Play conf folder, all files stored there are already in the app jar file
    mappings in Universal := {
      val universalMappings = (mappings in Universal).value
      val filtered = universalMappings filter {
        case (file, name) => !name.startsWith("share/") && (!name.startsWith("conf/") || name == "conf/application.ini")
      }
      filtered
    },

    linuxPackageMappings in Rpm := {
      val mappings = (linuxPackageMappings in Rpm).value
      mappings map { linuxPackage =>
        val filtered = linuxPackage.mappings filter {
          case (file, name) => !name.startsWith("/var/run") // this folder is added by the java_server archetype, we don't need it
        }
        linuxPackage.copy(
          mappings = filtered
        )
      }
    }

  )

  private def loadPluginResource(scriptletName: String, replacements: Seq[(String, String)]) = {
    val template = IO.readStream(getClass.getClassLoader.getResourceAsStream(s"rpm/scriptlets/$scriptletName"))
    val script = TemplateWriter.generateScriptFromString(template, replacements)
    script
  }

}
