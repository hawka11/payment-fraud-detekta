package detekta.payment.ingester

import iot.jcypher.database.DBAccessFactory
import iot.jcypher.database.DBProperties
import iot.jcypher.database.DBType
import iot.jcypher.database.IDBAccess
import java.util.*

fun neo4jDb(): IDBAccess {
    val props = Properties()
    props.setProperty(DBProperties.SERVER_ROOT_URI, "http://10.0.10.50:7474")
    return DBAccessFactory.createDBAccess(DBType.REMOTE, props, "neo4j", "fraud")
}