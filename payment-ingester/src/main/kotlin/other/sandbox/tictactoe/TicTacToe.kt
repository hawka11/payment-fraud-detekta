package other.sandbox.tictactoe

fun main(args: Array<String>) {

    fun decidePiece(g: Grid) =
            if (g.lastMove == null || g.lastMove.piece == Piece.X) Piece.O else Piece.X

    fun buildTree(): TreeNode {
        fun node(x: Int, y: Int, w: Double) = TreeNode(Coord(x, y), w, emptyList())

        val nextChoices = listOf(node(0, 0, 0.5), node(0, 1, 0.4))

        return TreeNode(Coord(1, 1), 1.0, nextChoices)
    }

    fun decideMove(g: Grid, t: TreeNode, p: Piece): Move? {
        //if any +1 loosing move, then block
        //else...
        return if (g.getAt(t.coord).piece == null) {
            Move(t.coord.x, t.coord.y, p)
        } else {
            val next = t.children
                    .sortedBy { it.weight }
                    .firstOrNull {
                        g.getAt(it.coord).piece == null }

            next?.let { Move(it.coord.x, it.coord.y, p) }
        }
    }

    val tree = buildTree()

    generateSequence(Grid.create(3, 3)) { grid ->

        val p = decidePiece(grid)
        val m = decideMove(grid, tree, p)

        m?.let { grid.makeMove(it) }

    }.take(5).toList().map(::println)
}


