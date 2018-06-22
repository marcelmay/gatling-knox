package de.m3y.gatling.knox

import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session


case class KnoxComponents(knoxProtocol: KnoxProtocol) extends ProtocolComponents {

  override def onStart: Option[(Session) => Session] = None

  override def onExit: Option[(Session) => Unit] = None

}
