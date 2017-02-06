package detekta.payment.fraudresolver

class Payment {

    var correlationId: String? = null
}

class Customer {

    var code: String? = null
    var payments: List<Payment> = emptyList()
}