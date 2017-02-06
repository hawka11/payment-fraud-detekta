package detekta.payment.fraudresolver.customer

import co.paralleluniverse.actors.behaviors.RequestMessage
import detekta.payment.fraudresolver.ResolveResult
import java.time.Duration

class FraudCheck(val customerCode: String, val duration: Duration) : RequestMessage<ResolveResult>()