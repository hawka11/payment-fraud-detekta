package other.sandbox.multicast

object MulticastServer {
    @Throws(java.io.IOException::class)
    @JvmStatic fun main(args: Array<String>) {
        MulticastServerThread().start()
    }
}