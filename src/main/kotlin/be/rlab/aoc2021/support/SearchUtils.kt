package be.rlab.aoc2021.support

object SearchUtils {
    fun<T> breadthFirstSearch(
        graph: Graph<T>,
        start: T,
        goal: T? = null,
        accepts: (T) -> Boolean = { true }
    ): MutableMap<T, T?> {
        val frontier = ArrayDeque(listOf(start))
        val cameFrom: MutableMap<T, T?> = mutableMapOf(start to null)

        while (frontier.isNotEmpty()) {
            val current: T = frontier.removeFirst()

            if (current == goal) {
                break
            }

            graph.neighbors(current).filter(accepts).forEach { next ->
                if (next !in cameFrom) {
                    frontier.addFirst(next)
                    cameFrom[next] = current
                }
            }
        }

        return cameFrom
    }
}
