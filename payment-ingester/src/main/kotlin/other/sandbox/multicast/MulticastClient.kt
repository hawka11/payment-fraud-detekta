package other.sandbox.multicast

import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

object MulticastClient {

    @Throws(IOException::class)
    @JvmStatic fun main(args: Array<String>) {

        val socket = MulticastSocket(4446)
        val address = InetAddress.getByName("228.0.0.4")
        socket.joinGroup(address)

        var packet: DatagramPacket

        // get a few quotes
        for (i in 0..4) {

            val buf = ByteArray(256)
            packet = DatagramPacket(buf, buf.size)
            socket.receive(packet)

            val received = String(packet.data, 0, packet.length)
            println("Quote of the Moment: " + received)
        }

        socket.leaveGroup(address)
        socket.close()
    }

}
