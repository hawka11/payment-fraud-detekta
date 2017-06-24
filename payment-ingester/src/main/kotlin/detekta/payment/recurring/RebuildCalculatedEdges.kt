package detekta.payment.recurring

import detekta.payment.cypher.neo4jDb
import detekta.payment.cypher.objectMapper

fun main(args: Array<String>) {

    val mapper = objectMapper()
    val neo4j = neo4jDb()
}

class RebuildCalculatedEdges {

}