package detekta.payment.ingester

import com.fasterxml.jackson.annotation.JsonCreator

data class PaymentResult @JsonCreator constructor(
        val createdAt: Long,
        val unitAmount: Int,
        val status: String,
        val errorCode: String?,
        val customerCode: String,
        val organisationCode: String,
        val accountCode: String,
        val merchantCode: String,
        val ipAddress: String,
        val token: String,
        val accountRef: String,
        val correlationId: String
)