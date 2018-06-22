package de.m3y.gatling.knox

import akka.actor.ActorSystem
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}
import org.apache.knox.gateway.shell.Hadoop

object KnoxProtocol {

  val knoxProtocolKey = new ProtocolKey {

    type Protocol = KnoxProtocol
    type Components = KnoxComponents

    def protocolClass: Class[io.gatling.core.protocol.Protocol] =
        classOf[KnoxProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): KnoxProtocol = throw new IllegalStateException("Not supported")

    def newComponents(system: ActorSystem, coreComponents: CoreComponents): KnoxProtocol => KnoxComponents = {

      knoxProtocol => {
        val knoxComponents = KnoxComponents(knoxProtocol)

        system.registerOnTermination {
          knoxProtocol.hadoop.close()
        }

        knoxComponents
      }
    }
  }
}

case class KnoxProtocol(gatewayUrl: String, username: String, password: String) extends Protocol {
  val hadoop = Hadoop.login(gatewayUrl, username, password)
}
