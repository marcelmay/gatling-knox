package de.m3y.gatling.knox

import org.apache.knox.gateway.shell.{AbstractRequest, Hadoop}

object Predef {
  def knox[T](requestName: String)(knoxAction: Hadoop => AbstractRequest[T]) = new KnoxRequestActionBuilder(requestName, knoxAction)
}