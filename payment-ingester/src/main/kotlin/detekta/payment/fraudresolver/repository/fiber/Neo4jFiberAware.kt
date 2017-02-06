package detekta.payment.fraudresolver.repository.fiber

import co.paralleluniverse.fibers.Suspendable
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import iot.jcypher.database.IDBAccess
import iot.jcypher.query.JcQuery
import iot.jcypher.query.JcQueryResult
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class Neo4jFiberAware(val neo4J: IDBAccess) {

    private var executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10))

    @Suspendable
    fun queryAndExecute(buildQuery: (q: JcQuery) -> Unit): JcQueryResult {

        val query = JcQuery()

        buildQuery(query)

        return object : Neo4jAsync() {
            override fun requestAsync() {

                val f = executor.submit(Callable { neo4J.execute(query) })

                Futures.addCallback(f, object : FutureCallback<JcQueryResult> {
                    override fun onFailure(t: Throwable): Unit = failure(t)

                    override fun onSuccess(result: JcQueryResult?): Unit = success(result!!)
                })
            }
        }.run()
    }
}
