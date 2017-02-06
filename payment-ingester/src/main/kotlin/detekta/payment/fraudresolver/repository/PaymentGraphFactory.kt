package detekta.payment.fraudresolver.repository

import iot.jcypher.query.values.JcNode
import iot.jcypher.query.values.JcRelation

fun customerNode() = JcNode("Customer")
fun paymentNode() = JcNode("Payment")
fun attemptedPaymentRel() = JcRelation("ATTEMPTED_PAYMENT")

