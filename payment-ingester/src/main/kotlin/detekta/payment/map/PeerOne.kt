package detekta.payment.map

import kotlin.concurrent.thread

fun main(args: Array<String>) {

    val grid = createGrid(1)
    val store = grid.store()

    thread {
        //val tree = TreeMap(store, "myRootName")
        //tree.init()
    }
}

