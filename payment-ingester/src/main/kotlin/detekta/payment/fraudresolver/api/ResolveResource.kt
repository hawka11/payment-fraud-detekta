package detekta.payment.fraudresolver.api

import co.paralleluniverse.fibers.Fiber
import co.paralleluniverse.fibers.Suspendable
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/resolve2")
@Produces(APPLICATION_JSON)
class ResolveResource {

    @GET
    @Suspendable
    fun get(): List<String> {
        println("fiber: ${Fiber.currentFiber()} :::::: Thread: ${Thread.currentThread().id}")
        return listOf("fdsa", "fdsa")
    }
}