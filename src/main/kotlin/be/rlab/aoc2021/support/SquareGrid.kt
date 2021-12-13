package be.rlab.aoc2021.support

interface Graph<T> {
    fun neighbors(item: T): List<T>
}

interface WeightedGraph<T> : Graph<T> {
    fun cost(from: T, to: T): Double
}

data class Tile(
    val x: Int,
    val y: Int
) {
    fun translateToIndex(width: Int): Int {
        return y * width + x
    }

    fun moveX(distance: Int): Tile = copy(
        x = x + distance
    )

    fun moveY(distance: Int): Tile = copy(
        y = y + distance
    )

    fun move(
        distanceX: Int,
        distanceY: Int
    ): Tile = copy(
        x = x + distanceX,
        y = y + distanceY
    )
}

open class SquareGrid(
    val width: Int,
    val height: Int,
    val includeVertices: Boolean = false
) : Graph<Tile> {
    override fun neighbors(item: Tile): List<Tile> {
        val vertices = if (includeVertices) {
            listOf(item.move(1, -1), item.move(1, 1), item.move(-1, -1), item.move(-1, 1))
        } else {
            emptyList()
        }
        val edges = listOf(item.moveX(1), item.moveX(-1), item.moveY(-1), item.moveY(1))
        val neighbors = (vertices + edges).filter(::inBounds)

        return if ((item.x + item.y) % 2 == 0) {
            neighbors.reversed()
        } else {
            neighbors
        }
    }

    private fun inBounds(tile: Tile): Boolean {
        return (tile.x in 0 until width) && (tile.y in 0 until height)
    }
}

open class SimpleGraph<T>(
    private val edges: Map<T, List<T>>
) : Graph<T> {
    override fun neighbors(item: T): List<T> {
        return edges[item] ?: emptyList()
    }
}
