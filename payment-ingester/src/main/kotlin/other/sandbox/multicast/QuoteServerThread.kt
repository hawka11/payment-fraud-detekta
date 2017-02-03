package other.sandbox.multicast

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.*

open class QuoteServerThread @Throws(IOException::class)
@JvmOverloads constructor(name: String = "QuoteServerThread") : Thread(name) {

    protected var socket: DatagramSocket? = null
    protected var `in`: BufferedReader? = null
    protected var moreQuotes = true

    init {
        socket = DatagramSocket(4445)

        try {
            `in` = BufferedReader(FileReader("one-liners.txt"))
        } catch (e: FileNotFoundException) {
            System.err.println("Could not open quote file. Serving time instead.")
        }

    }

    override fun run() {

        while (moreQuotes) {
            try {
                var buf = ByteArray(256)

                // receive request
                var packet = DatagramPacket(buf, buf.size)
                socket!!.receive(packet)

                // figure out response
                val dString = if (`in` == null)
                    Date().toString()
                else
                    nextQuote()

                buf = dString.toByteArray()

                // send the response to the client at "address" and "port"
                val address = packet.address
                val port = packet.port
                packet = DatagramPacket(buf, buf.size, address, port)
                socket!!.send(packet)
            } catch (e: IOException) {
                e.printStackTrace()
                moreQuotes = false
            }

        }
        socket!!.close()
    }

    protected fun nextQuote(): String {
        return try {
            val returnValue = `in`!!.readLine()

            return if (returnValue == null) {
                `in`!!.close()
                moreQuotes = false
                return "No more quotes. Goodbye."
            } else returnValue

        } catch (e: IOException) {
            "IOException occurred in server."
        }
    }
}
