package detekta.payment.ingester

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import iot.jcypher.graph.GrNode
import iot.jcypher.graph.Graph

fun main(args: Array<String>) {

    val mapper = objectMapper()
    val neo4j = neo4jDb()
    val graph = Graph.create(neo4j)

    LoadInitialNeo4jData().loadPayments(graph, mapper)
}

class LoadInitialNeo4jData {

    private val customerNodeByCode = hashMapOf<String, GrNode>()

    fun loadPayments(graph: Graph, mapper: ObjectMapper) {

        val resultsFile = javaClass.classLoader.getResource("import/paymentresults/paymentResults.json")
        val results: List<PaymentResult> =
                mapper.readValue(resultsFile, object : TypeReference<List<PaymentResult>>() {})

        loadPaymentResults(graph, results)
    }

    private fun loadPaymentResults(graph: Graph, results: List<PaymentResult>) {
        results.forEach {

            val customer = getOrCreateCustomer(graph, it.customerCode)
            val resultNode = graph.createNode()

            resultNode.addLabel("Token")
            resultNode.addProperty("token", it.token)
            resultNode.addProperty("status", it.status)
            resultNode.addProperty("createdAt", it.createdAt)
            resultNode.addProperty("unitAmount", it.unitAmount)
            resultNode.addProperty("ipAddress", it.ipAddress)

            graph.createRelation("ATTEMPTED_PAYMENT", customer, resultNode)

            graph.store()
        }
    }

    private fun getOrCreateCustomer(graph: Graph, customerCode: String): GrNode {
        if (!customerNodeByCode.containsKey(customerCode)) {
            val customer = graph.createNode()
            customer.addLabel("Customer")
            customer.addProperty("code", customerCode)
            customerNodeByCode[customerCode] = customer
        }
        return customerNodeByCode[customerCode]!!
    }
}