package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.Point
import be.rlab.aoc2021.support.ResourceUtils.loadInput

/** Day 9: Smoke Basin
 *
 * This challenge is about finding points in a plane with different strategies.
 *
 * This solution uses a list and some basic geometry math to calculate coordinates of points in a plane.
 * The plane is a map, and the points represent heights of the terrain.
 *
 * In the first part we need to find "holes", terrains (points) that are surrounded by higher terrains. It
 * implies resolving all neighbor terrains (left, right, top, bottom) for each terrain in the map, and count
 * those that are lower than the neighbors.
 *
 * The second part is about finding "basins". A basin is the group of terrains that "flows" to a lower terrain.
 * Think of the basin as all the terrains that have a descending slope to the lower terrain, like a river that flows
 * from a higher to a lower terrain. The higher value for a terrain height is 9 (the peek), so this algorithm
 * searches from the lower terrain through all the neighbors recursively until it finds the peek.
 */

const val TERRAIN_PEEK: Int = 9

data class Terrain(
    val height: Int,
    val index: Int
)

data class HeightMap(
    private val columnCount: Int,
    private val rowCount: Int,
    private val points: List<Terrain>
) {
    val lowPoints: List<Terrain> get() = points.mapIndexedNotNull { index, point ->
        val neighbors: List<Terrain> = findNeighbors(index)
        if (point.height < neighbors.minOfOrNull { it.height }!!) {
            point
        } else {
            null
        }
    }

    fun basinSize(
        terrain: Terrain,
        knownNeighbors: MutableSet<Int> = mutableSetOf()
    ): Int {
        val neighbors: List<Terrain> = findNeighbors(terrain.index).filter { neighbor ->
            neighbor.height < TERRAIN_PEEK && !knownNeighbors.contains(neighbor.index)
        }
        knownNeighbors += (knownNeighbors + neighbors.map { it.index }) + terrain.index

        return neighbors.sumOf { neighbor ->
            1 + basinSize(neighbor, knownNeighbors)
        }
    }

    private fun findNeighbors(index: Int): List<Terrain> {
        return Point.neighbors(
            point = Point(columnCount, rowCount, index),
            vertices = false
        ).map { neighbor ->
            points[neighbor.index]
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()
    val lines = loadInput("09-smoke-basin.txt").split("\n")
    val heightMap = HeightMap(
        columnCount = lines[0].length,
        rowCount = lines.size,
        points = lines.flatMap { line ->
            line.split("").filter { it.isNotEmpty() }.map { digit -> digit.toInt() }
        }.mapIndexed { index, height -> Terrain(height, index) }
    )
    val totalLowPoints = heightMap.lowPoints.sumOf { terrain ->
        terrain.height + 1
    }
    val basinSizes = heightMap.lowPoints.map { terrain ->
        heightMap.basinSize(terrain) + 1
    }.sortedDescending()

    println("total low points: $totalLowPoints")
    println("largest basins: ${basinSizes[0] * basinSizes[1] * basinSizes[2]}")
    println("${System.currentTimeMillis() - start}ms")
}
