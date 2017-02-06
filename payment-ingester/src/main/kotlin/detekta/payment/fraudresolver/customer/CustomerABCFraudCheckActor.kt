package detekta.payment.fraudresolver.customer

import co.paralleluniverse.actors.BasicActor
import co.paralleluniverse.fibers.Suspendable
import detekta.payment.fraudresolver.ResolveResult
import detekta.payment.fraudresolver.repository.fiber.Neo4jFiberAware
import detekta.payment.fraudresolver.rule.VelocityNumberPaymentWithinDurationActor
import detekta.payment.fraudresolver.rule.VelocityRequest

class CustomerABCFraudCheckActor(neo4j: Neo4jFiberAware) : BasicActor<Any, ResolveResult>() {

    private val ruleOne = VelocityNumberPaymentWithinDurationActor(neo4j, 1500.0).spawn()
    private val ruleTwo = VelocityNumberPaymentWithinDurationActor(neo4j, 1200.0).spawn()

    private val initReceive = { it: Any ->
        when (it) {
            is FraudCheck -> {
                ruleOne.send(VelocityRequest(it.customerCode, 5, it.duration))
                ruleTwo.send(VelocityRequest(it.customerCode, 2, it.duration))
            }
            else -> null
        }
    }

    private val waitingForResponses = { it: Any ->
        when (it) {
        //is ResolveResult -> {
            else -> println("${System.currentTimeMillis()} -> $it")
        //}
        }
    }

    @Suspendable
    override fun doRun(): ResolveResult {

        receive(initReceive)

        while (true) {
            receive(waitingForResponses)
        }
    }
}