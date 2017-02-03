package detekta.payment.map

import co.paralleluniverse.strands.Strand
import kotlin.concurrent.thread

fun main(args: Array<String>) {

    val grid = createGrid(2)
    val store = grid.store()
    val messenger = grid.messenger()


    thread {
        Strand.sleep(1000)
        val tree = TreeMap<Int>(store, messenger, "myRootName")

        tree.put("A", 5)
        tree.put("B", 10)
        //tree.init()
        //       val get = tree.get()
        println(tree.get("B"))
    }
}

