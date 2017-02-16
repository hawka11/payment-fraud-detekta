package detekta.payment.fraudresolver.customer

import co.paralleluniverse.actors.BasicActor
import co.paralleluniverse.actors.MessageProcessor
import co.paralleluniverse.actors.behaviors.RequestReplyHelper
import co.paralleluniverse.fibers.Suspendable
import detekta.payment.fraudresolver.ResolveResult
import detekta.payment.fraudresolver.aggregateResults
import detekta.payment.fraudresolver.repository.fiber.Neo4jFiberAware
import detekta.payment.fraudresolver.rule.VelocityNumberPaymentsWithinDurationActor
import detekta.payment.fraudresolver.rule.VelocityRequest

class CustomerABCFraudCheckActor(neo4j: Neo4jFiberAware) : BasicActor<Any, ResolveResult>() {

    private val ruleOne = VelocityNumberPaymentsWithinDurationActor(neo4j, 1500.0).spawn()
    private val ruleTwo = VelocityNumberPaymentsWithinDurationActor(neo4j, 1200.0).spawn()

    private var origReq: FraudCheck? = null

    private val initReceive = { msg: Any ->
        when (msg) {
            is FraudCheck -> {
                origReq = msg
                ruleOne.send(VelocityRequest(msg.customerCode, 2, msg.start, msg.end))
                ruleTwo.send(VelocityRequest(msg.customerCode, 2, msg.start, msg.end))
            }
            else -> null
        }
    }

    //TODO: Nested / Recursive selective receive isn't working perfect, need to investigate...but this works
    fun waitForResponses(resp: List<ResolveResult>, remaining: Int): MessageProcessor<Any, List<ResolveResult>> {
        return MessageProcessor { msg: Any ->
            when (msg) {
                is ResolveResult -> if (remaining <= 1) resp + msg else
                    receive(waitForResponses(resp + msg, remaining - 1))
                else -> receive(waitForResponses(resp, remaining))
            }
        }
    }

    @Suspendable
    override fun doRun(): ResolveResult {

        receive(initReceive)

        val results = receive(waitForResponses(listOf(), 2))
        val total = aggregateResults(results)

        RequestReplyHelper.reply(origReq, total)

        return total
    }
}