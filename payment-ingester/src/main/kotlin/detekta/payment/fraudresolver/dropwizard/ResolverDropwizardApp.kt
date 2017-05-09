package detekta.payment.fraudresolver.dropwizard

import be.tomcools.dropwizard.websocket.WebsocketBundle
import co.paralleluniverse.actors.ActorRegistry
import co.paralleluniverse.comsat.webactors.servlet.WebActorInitializer
import co.paralleluniverse.fibers.dropwizard.FiberApplication
import detekta.payment.fraudresolver.api.ResolveResource
import detekta.payment.fraudresolver.customer.CustomerABCFraudCheckActor
import detekta.payment.fraudresolver.repository.fiber.Neo4jFiberAware
import detekta.payment.ingester.neo4jDb
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.eclipse.jetty.server.session.SessionHandler

fun main(args: Array<String>) {
    ResolverDropwizardApp().run(*args)
}

class ResolverDropwizardApp : FiberApplication<ResolverConfig>() {

    private val websocket = WebsocketBundle<ResolverConfig>()

    override fun initialize(bootstrap: Bootstrap<ResolverConfig>) {
        super.initialize(bootstrap)
        bootstrap.addBundle(websocket)
    }

    override fun fiberRun(config: ResolverConfig, env: Environment) {

        env.servlets().setSessionHandler(SessionHandler())

        env.jersey().register(ResolveResource())

        WebActorInitializer.setUserClassLoader(ClassLoader.getSystemClassLoader());
        env.servlets().addServletListeners(WebActorInitializer())

        val neo4j = Neo4jFiberAware(neo4jDb())
        ActorRegistry.getOrRegisterActor("customerABCFraudCheckActor") { CustomerABCFraudCheckActor(neo4j) }
    }
}

class ResolverConfig : Configuration()