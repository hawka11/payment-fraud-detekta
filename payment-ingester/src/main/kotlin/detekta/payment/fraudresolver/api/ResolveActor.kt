package detekta.payment.fraudresolver.api

import co.paralleluniverse.actors.ActorRef
import co.paralleluniverse.actors.ActorRegistry
import co.paralleluniverse.actors.BasicActor
import co.paralleluniverse.actors.behaviors.RequestReplyHelper
import co.paralleluniverse.comsat.webactors.HttpRequest
import co.paralleluniverse.comsat.webactors.HttpResponse
import co.paralleluniverse.comsat.webactors.WebActor
import co.paralleluniverse.comsat.webactors.WebMessage
import co.paralleluniverse.fibers.Suspendable
import detekta.payment.fraudresolver.customer.FraudCheck
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

@WebActor(name = "resolve", httpUrlPatterns = arrayOf("/resolve"), webSocketUrlPatterns = arrayOf("/resolve/ws"))
class ResolveActor : BasicActor<WebMessage, Void>() {

    override @Suspendable fun doRun(): Void? {

        while (true) {
            val receive = receive()

            when (receive) {
                is HttpRequest -> {
                    val start = OffsetDateTime.of(2017, 2, 15, 6, 7, 49, 0, UTC)
                    val end = OffsetDateTime.of(2017, 2, 16, 6, 8, 10, 0, UTC)
                    val req = FraudCheck("JOED123", start.toInstant(), end.toInstant())

                    val abcActor = ActorRegistry.tryGetActor<ActorRef<FraudCheck>>("customerABCFraudCheckActor")
                    val resp = RequestReplyHelper.call(abcActor, req)

                    receive.from.send(HttpResponse.ok(ref(), receive, resp.toString()).build())
                }
            }
        }
    }
}