package de.m3y.gatling.knox

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import org.apache.knox.gateway.shell.{AbstractRequest, Hadoop}


class KnoxRequestActionBuilder[T](actionName: String, knoxRequest: Hadoop => AbstractRequest[T]) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx.{coreComponents, protocolComponentsRegistry, throttled}

    val knoxComponents: KnoxComponents = protocolComponentsRegistry.components(KnoxProtocol.knoxProtocolKey)

    new KnoxRequestAction(
      actionName,
      knoxRequest,
      coreComponents.statsEngine,
      knoxComponents.knoxProtocol,
      throttled,
      next
    )

  }
}