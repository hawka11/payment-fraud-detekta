package detekta.payment.fraudresolver.customer

import co.paralleluniverse.actors.behaviors.RequestMessage
import detekta.payment.fraudresolver.ResolveResult
import java.time.Instant

class FraudCheck(val customerCode: String, val start: Instant, val end: Instant) : RequestMessage<ResolveResult>()