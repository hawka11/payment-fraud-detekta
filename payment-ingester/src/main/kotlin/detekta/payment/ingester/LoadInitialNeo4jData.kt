package detekta.payment.ingester

import iot.jcypher.database.DBAccessFactory
import iot.jcypher.database.DBProperties.SERVER_ROOT_URI
import iot.jcypher.database.DBType.REMOTE
import java.util.*

class LoadInitialNeo4jData {

    fun main(args: Array<String>) {

        val props = Properties()

        props.setProperty(SERVER_ROOT_URI, "http://10.0.10.50:7474")

        val remote = DBAccessFactory.createDBAccess(REMOTE, props)
    }
}