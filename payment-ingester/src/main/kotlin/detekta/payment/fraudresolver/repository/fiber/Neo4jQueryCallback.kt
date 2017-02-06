package detekta.payment.fraudresolver.repository.fiber

import co.paralleluniverse.fibers.FiberAsync
import iot.jcypher.query.JcQueryResult

interface Neo4jQueryCallback {

    fun success(result: JcQueryResult)

    fun failure(t: Throwable)
}

abstract class Neo4jAsync : FiberAsync<JcQueryResult, Exception>(), Neo4jQueryCallback {

    override fun success(result: JcQueryResult): Unit = asyncCompleted(result)

    override fun failure(t: Throwable): Unit = asyncFailed(t)
}