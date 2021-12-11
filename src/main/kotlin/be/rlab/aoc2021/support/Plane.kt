package be.rlab.aoc2021.support

/** Enumeration of positions of a [Point] relative to a plane.
 */
enum class Position {
    TOP,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    LEFT,
    RIGHT,
    CENTER
}

data class Point(
    val width: Int,
    val height: Int,
    val index: Int
) {
    companion object {
        fun neighbors(
            point: Point,
            vertices: Boolean
        ): List<Point> {
            return when(point.position) {
                Position.TOP_LEFT -> listOf(point.right, point.bottom, point.bottomRight.takeIf { vertices })
                Position.TOP_RIGHT -> listOf(point.left, point.bottom, point.bottomLeft.takeIf { vertices })
                Position.TOP -> listOf(
                    point.left, point.right, point.bottom,
                    point.bottomLeft.takeIf { vertices }, point.bottomRight.takeIf { vertices }
                )
                Position.BOTTOM_LEFT -> listOf(point.top, point.right, point.topRight.takeIf { vertices })
                Position.BOTTOM_RIGHT -> listOf(point.left, point.top, point.topLeft.takeIf { vertices })
                Position.BOTTOM -> listOf(
                    point.left, point.right, point.top,
                    point.topLeft.takeIf { vertices }, point.topRight.takeIf { vertices }
                )
                Position.LEFT -> listOf(
                    point.right, point.top, point.bottom,
                    point.topRight.takeIf { vertices }, point.bottomRight.takeIf { vertices }
                )
                Position.RIGHT -> listOf(
                    point.left, point.bottom, point.top,
                    point.bottomLeft.takeIf { vertices }, point.topLeft.takeIf { vertices }
                )
                Position.CENTER -> listOf(
                    point.left, point.right, point.bottom, point.top,
                    point.bottomRight.takeIf { vertices }, point.bottomLeft.takeIf { vertices },
                    point.topLeft.takeIf { vertices }, point.topRight.takeIf { vertices }
                )
            }.filterNotNull()
        }
    }

    private val row: Int = index / width
    private val column: Int = index % width

    /** All adjacent points. */
    val right: Point get() = copy(index = index + 1)
    val left: Point get() = copy(index = index - 1)
    val top: Point get() = copy(index = index - width)
    val bottom: Point get() = copy(index = index + width)
    val topLeft: Point get() = copy(index = index - width - 1)
    val topRight: Point get() = copy(index = index - width + 1)
    val bottomLeft: Point get() = copy(index = index + width - 1)
    val bottomRight: Point get() = copy(index = index + width + 1)

    /** Point position in the plane. */
    val position: Position = when {
        row == 0 && column == 0 -> Position.TOP_LEFT
        row == 0 && column == width - 1 -> Position.TOP_RIGHT
        row == 0 -> Position.TOP
        row == height - 1 && column == 0 -> Position.BOTTOM_LEFT
        row == height - 1 && column == width - 1 -> Position.BOTTOM_RIGHT
        row == height - 1 -> Position.BOTTOM
        column == 0 -> Position.LEFT
        column == width - 1 -> Position.RIGHT
        else -> Position.CENTER
    }
}
