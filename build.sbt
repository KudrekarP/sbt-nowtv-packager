import sbt.Keys._

name := "sbt-nowtv-packager"

version := "2.1.5"

organization := "com.nowtv"

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6")

parallelExecution in Test := false

fork in Test := false

libraryDependencies += "joda-time" % "joda-time" % "2.9.4"

resolvers ++= Seq(
  "typesafe-snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/",
  "maven" at " https://repo1.maven.org/maven2/"
)

publishTo := Some("Artifactory Realm" at "https://nexus.api.bskyb.com/nexus/content/repositories/sky-releases/")

credentials += Credentials(
  "Artifactory Realm",
  "buildrepo.nowtv.bskyb.com",
  "admin",
  "G00dygumdrops"
)

publishArtifact in Test := false

pomIncludeRepository := { _ => false }