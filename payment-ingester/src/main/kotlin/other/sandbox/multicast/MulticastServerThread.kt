package other.sandbox.multicast

import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.*

class MulticastServerThread @Throws(IOException::class)
constructor() : QuoteServerThread("MulticastServerThread") {

    private val FIVE_SECONDS: Long = 5000

    override fun run() {
        while (moreQuotes) {
            try {
                // construct quote
                var dString: String? = null
                if (`in` == null)
                    dString = Date().toString()
                else
                    dString = nextQuote()

                val buf = dString.toByteArray()

                // send it
                val group = InetAddress.getByName("228.0.0.4")
                val packet = DatagramPacket(buf, buf.size, group, 4446)
                socket!!.send(packet)

                // sleep for a while
                try {
                    sleep((Math.random() * FIVE_SECONDS).toLong())
                } catch (e: InterruptedException) {
                }

            } catch (e: IOException) {
                e.printStackTrace()
                moreQuotes = false
            }
        }

        socket!!.close()
    }
}
