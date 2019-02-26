package com.nowtv.sbt.packager.rpm

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

trait VersionSettings {

  private val formatter = DateTimeFormat.forPattern("yyyyMMdd.HHmm")

  def currentTimestamp = formatter.print(DateTime.now())

}
