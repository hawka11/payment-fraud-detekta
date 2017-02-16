package detekta.payment.fraudresolver

import co.paralleluniverse.actors.behaviors.RequestReplyHelper
import detekta.payment.fraudresolver.customer.CustomerABCFraudCheckActor
import detekta.payment.fraudresolver.customer.FraudCheck
import detekta.payment.fraudresolver.repository.fiber.Neo4jFiberAware
import detekta.payment.ingester.neo4jDb
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

fun main(args: Array<String>) {

    val neo4j = Neo4jFiberAware(neo4jDb())

    val actor = CustomerABCFraudCheckActor(neo4j).spawn()

    val start = OffsetDateTime.of(2017, 2, 15, 6, 7, 49, 0, UTC)
    val end = OffsetDateTime.of(2017, 2, 16, 6, 8, 10, 0, UTC)
    val req = FraudCheck("JOED123", start.toInstant(), end.toInstant())

    val result = RequestReplyHelper.call(actor, req)
    println(result)
}


