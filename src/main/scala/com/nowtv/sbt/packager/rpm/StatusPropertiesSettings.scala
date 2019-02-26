package com.nowtv.sbt.packager.rpm

import com.typesafe.sbt.SbtNativePackager._
import sbt.Keys._
import sbt._

trait StatusPropertiesSettings extends ConfigNameSettings with RpmNameSettings {

  def statusPropertiesSettings = Seq[Def.Setting[_]](
    resourceGenerators in Compile <+= (resourceManaged in Compile, version in Rpm, rpmNameTask, name in Rpm) map { (dir, version, rpmName, appName) =>
      val file = dir / "status.properties"
      val fixedApplicationProperties: Map[String, Option[Any]] = Map(
        "app.name" -> Some(appName),
        "daily.version" -> Some(version),
        "app.rpm.name" -> rpmName
      )
      val variablePropertyKeys: Map[String, String] = Map(
        "JENKINS_URL" -> "build.server",
        "JOB_NAME" -> "build.job",
        "BUILD_NUMBER" -> "build.number",
        "BUILD_ID" -> "build.identifier",
        "GIT_BRANCH" -> "git.branch",
        "GIT_COMMIT" -> "git.commit"
      )
      val variableApplicationProperties: Map[String, Option[Any]] = variablePropertyKeys.keys.map { key => variablePropertyKeys(key) -> sys.env.get(key) }.toMap
      val mergedPropertiesMap = (fixedApplicationProperties ++ variableApplicationProperties).collect {
        case (key, Some(value)) => key -> value
      }
      IO.write(file, mergedPropertiesMap.toSeq.collect { case (key, value) => s"$key=$value\n" }.mkString)
      Seq(file)
    }
  )

}
