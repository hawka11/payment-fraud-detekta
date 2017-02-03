package detekta.payment.map

import co.paralleluniverse.galaxy.Grid

fun createGrid(nodeId: Int): Grid {

    System.setProperty("galaxy.nodeId", Integer.toString(nodeId))
    System.setProperty("galaxy.port", Integer.toString(7050 + nodeId))
    System.setProperty("galaxy.slave_port", Integer.toString(8050 + nodeId))
    System.setProperty("galaxy.multicast.address", "228.0.0.4")
    System.setProperty("galaxy.multicast.port", "4446")

    val grid = Grid.getInstance(null, System.getProperties())

    grid.goOnline()

    return grid
}