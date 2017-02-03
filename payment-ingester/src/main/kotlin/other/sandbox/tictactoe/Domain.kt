package other.sandbox.tictactoe

class Grid(private val grid: List<List<Cell>>, val lastMove: Move?) {

    private fun List<Cell>.makeXMove(move: Move) =
            mapIndexed { x, p -> if (move.x == x) Cell(move.piece) else p }

    fun makeMove(move: Move) =
            Grid(grid.mapIndexed { y, l -> if (move.y == y) l.makeXMove(move) else l }, move)

    fun getAt(c: Coord) = grid[c.x][c.y]

    companion object Grid {
        private fun arrayOfCells(size: Int) =
                IntRange(1, size).map { Cell(null) }

        private fun _create(x: Int, y: Int) =
                IntRange(1, y).map { arrayOfCells(x) }

        fun create(x: Int, y: Int) = Grid(_create(x, y), null)
    }

    override fun toString() = grid.toString()
}

data class TreeNode(val coord: Coord, val weight: Double, val children: List<TreeNode>)
data class Coord(val x: Int, val y: Int)

data class Cell(val piece: Piece?)
data class Move(val x: Int, val y: Int, val piece: Piece)
sealed class Piece() {
    object X : Piece()
    object O : Piece()
}
