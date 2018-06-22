package de.m3y.gatling.knox

object KnoxProtocolBuilder {
  implicit def toKnoxProtocol(builder: KnoxProtocolBuilder): KnoxProtocol = builder.build

  def apply(gatewayUrl: String, username: String, password: String): KnoxProtocolBuilder =
    KnoxProtocolBuilder(KnoxProtocol(gatewayUrl, username, password))
}


case class KnoxProtocolBuilder(knoxProtocol: KnoxProtocol) {
  def build = knoxProtocol
}