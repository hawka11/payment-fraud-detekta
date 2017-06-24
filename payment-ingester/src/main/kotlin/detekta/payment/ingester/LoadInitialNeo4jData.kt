package detekta.payment.ingester

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import detekta.payment.cypher.neo4jDb
import detekta.payment.cypher.objectMapper
import iot.jcypher.graph.GrNode
import iot.jcypher.graph.Graph

fun main(args: Array<String>) {

    val mapper = objectMapper()
    val neo4j = neo4jDb()
    val graph = Graph.create(neo4j)

    neo4j.clearDatabase()

    LoadInitialNeo4jData().loadPayments(graph, mapper)
}

class LoadInitialNeo4jData {

    private val customerNodeByCode = hashMapOf<String, GrNode>()
    private val tokenNodeByToken = hashMapOf<String, GrNode>()
    private val ipNodeByIp = hashMapOf<String, GrNode>()
    private val paymentNodeById = hashMapOf<String, GrNode>()

    fun loadPayments(graph: Graph, mapper: ObjectMapper) {

        val resultsFile = javaClass.classLoader.getResource("import/paymentresults/paymentResults.json")
        val results: List<PaymentResult> =
                mapper.readValue(resultsFile, object : TypeReference<List<PaymentResult>>() {})

        loadPaymentResults(graph, results)
    }

    private fun loadPaymentResults(graph: Graph, results: List<PaymentResult>) {
        results.forEach {

            val customer = getOrCreateCustomer(graph, it)
            val paymentNode = getOrCreatePayment(graph, it)
            val tokenNode = getOrCreateToken(graph, it)
            val ipNode = getOrCreateIpAddress(graph, it)

            graph.createRelation("ATTEMPTED_PAYMENT", customer, paymentNode)
            graph.createRelation("USING_TOKEN", paymentNode, tokenNode)
            graph.createRelation("USING_IP", paymentNode, ipNode)

            graph.store()
        }
    }

    private fun getOrCreatePayment(graph: Graph, r: PaymentResult): GrNode {
        if (!paymentNodeById.containsKey(r.correlationId)) {

            val tokenNode = graph.createNode()

            tokenNode.addLabel("Payment")
            tokenNode.addProperty("status", r.status)
            tokenNode.addProperty("createdAt", r.createdAt)
            tokenNode.addProperty("unitAmount", r.unitAmount)
            tokenNode.addProperty("correlationId", r.correlationId)
            tokenNode.addProperty("latitude", r.latitude)
            tokenNode.addProperty("longitude", r.longitude)

            paymentNodeById[r.correlationId] = tokenNode
        }

        return paymentNodeById[r.correlationId]!!
    }

    private fun getOrCreateToken(graph: Graph, r: PaymentResult): GrNode {
        if (!tokenNodeByToken.containsKey(r.token)) {

            val tokenNode = graph.createNode()

            tokenNode.addLabel("Token")
            tokenNode.addProperty("token", r.token)
            tokenNode.addProperty("status", r.status)
            tokenNode.addProperty("createdAt", r.createdAt)
            tokenNode.addProperty("unitAmount", r.unitAmount)
            tokenNode.addProperty("ipAddress", r.ipAddress)

            tokenNodeByToken[r.token] = tokenNode
        }

        return tokenNodeByToken[r.token]!!
    }

    private fun getOrCreateCustomer(graph: Graph, r: PaymentResult): GrNode {
        if (!customerNodeByCode.containsKey(r.customerCode)) {
            val customer = graph.createNode()
            customer.addLabel("Customer")
            customer.addProperty("code", r.customerCode)
            customer.addProperty("createdAt", r.createdAt)
            customerNodeByCode[r.customerCode] = customer
        }
        return customerNodeByCode[r.customerCode]!!
    }

    private fun getOrCreateIpAddress(graph: Graph, r: PaymentResult): GrNode {
        if (!ipNodeByIp.containsKey(r.ipAddress)) {
            val customer = graph.createNode()
            customer.addLabel("IpAddress")
            customer.addProperty("ip", r.ipAddress)
            customer.addProperty("createdAt", r.createdAt)
            ipNodeByIp[r.ipAddress] = customer
        }
        return ipNodeByIp[r.ipAddress]!!
    }
}