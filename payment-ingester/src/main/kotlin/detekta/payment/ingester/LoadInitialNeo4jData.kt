package detekta.payment.ingester

import iot.jcypher.database.DBAccessFactory
import iot.jcypher.database.DBProperties.SERVER_ROOT_URI
import iot.jcypher.database.DBType.REMOTE
import iot.jcypher.database.IDBAccess
import iot.jcypher.query.JcQuery
import iot.jcypher.query.factories.clause.CREATE
import iot.jcypher.query.factories.clause.MATCH
import iot.jcypher.query.factories.clause.RETURN
import iot.jcypher.query.values.JcNode
import java.util.*

fun main(args: Array<String>) {

    val neo4j = neo4jDb()

    insertPayments(neo4j)

    exportAndPrintTomHanks(neo4j)
}

private fun neo4jDb(): IDBAccess {
    val props = Properties()
    props.setProperty(SERVER_ROOT_URI, "http://10.0.10.50:7474")
    return DBAccessFactory.createDBAccess(REMOTE, props, "neo4j", "fraud")
}

private fun insertPayments(neo4j: IDBAccess) {
    val token = JcNode("token123")

    val query = JcQuery()
    query.clauses = arrayOf(
            CREATE.node(token).label("Token").property("token").value("123")
    )

    val r = neo4j.execute(query)

    println(r.jsonResult)
}

private fun exportAndPrintTomHanks(neo4j: IDBAccess) {
    //"MATCH (tom {name: "Tom Hanks"}) RETURN tom"
    val person = JcNode("person")
    val hanks = MATCH.node(person).property("name").value("Tom Hanks")
    val returnHanks = RETURN.value(person)//.property("graph"))

    val query = JcQuery()
    query.clauses = arrayOf(hanks, returnHanks)

    val r = neo4j.execute(query)

    println(r.jsonResult)
}