package detekta.payment.fraudresolver.rule

import co.paralleluniverse.actors.KotlinActorSupport
import co.paralleluniverse.actors.behaviors.RequestMessage
import co.paralleluniverse.actors.behaviors.RequestReplyHelper
import co.paralleluniverse.fibers.Suspendable
import detekta.payment.fraudresolver.ResolveResult
import detekta.payment.fraudresolver.repository.attemptedPaymentRel
import detekta.payment.fraudresolver.repository.customerNode
import detekta.payment.fraudresolver.repository.fiber.Neo4jFiberAware
import detekta.payment.fraudresolver.repository.paymentNode
import iot.jcypher.query.factories.clause.MATCH
import iot.jcypher.query.factories.clause.RETURN
import java.time.Duration

class VelocityRequest(val customerCode: String,
                      val maxNumberPayments: Int,
                      val duration: Duration)
    : RequestMessage<ResolveResult>(RequestReplyHelper.from<ResolveResult>(), RequestReplyHelper.makeId())

private val DEFAULT_WEIGHT = 2000.0

class VelocityNumberPaymentsWithinDurationActor(
        val neo4j: Neo4jFiberAware,
        val weight: Double = DEFAULT_WEIGHT) : KotlinActorSupport<Any, ResolveResult>() {

    @Suspendable
    override fun doRun(): ResolveResult? {
        while (true) {
            receive {

                when (it) {
                    is VelocityRequest -> {
                        val res = neo4j.queryAndExecute { q ->
                            q.setClauses(arrayOf(
                                    MATCH.node(customerNode())
                                            .property("code").value(it.customerCode)
                                            .relation(attemptedPaymentRel()).node(paymentNode()),
                                    RETURN.ALL()
                            ))
                        }

                        val payments = res.resultOf(paymentNode())

                        val calculatedWeight = if (payments.size > it.maxNumberPayments) weight else 0.0

                        it.from.send(ResolveResult(calculatedWeight, null))
                    }
                    else -> null
                }
            }
        }
    }
}