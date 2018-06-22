package de.m3y.gatling.knox

import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.ClockSingleton._
import io.gatling.commons.validation._
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session._
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen
import org.apache.knox.gateway.shell.{AbstractRequest, Hadoop}


class KnoxRequestAction[T](val actionName: String,
                           val knoxRequest: Hadoop => AbstractRequest[T],
                           val statsEngine: StatsEngine,
                           val knoxProtocol: KnoxProtocol,
                           val throttled: Boolean,
                           val next: Action)
  extends ExitableAction with NameGen {

  override val name = genName(actionName)

  private def sendRequest(requestName: String,
                          knoxRequest: AbstractRequest[T],
                          session: Session): Validation[_] = {
    val requestStartDate = nowMillis
    try {
      val result = knoxRequest.now()
      val requestEndDate = nowMillis
      statsEngine.logResponse(
        session,
        requestName,
        ResponseTimings(startTimestamp = requestStartDate, endTimestamp = requestEndDate),
        OK,
        None,
        None
      )
      session.markAsSucceeded
      result.success
    } catch {
      case e: Exception =>
        val requestEndDate = nowMillis
        statsEngine.logResponse(
          session,
          requestName,
          ResponseTimings(startTimestamp = requestStartDate, endTimestamp = requestEndDate),
          KO,
          None,
          Some(e.getMessage)
        )
        session.markAsFailed
        e.getMessage().failure
    }
  }

  override def execute(session: Session): Unit = {
    sendRequest(name, knoxRequest(knoxProtocol.hadoop), session)
    next ! session
  }

}
