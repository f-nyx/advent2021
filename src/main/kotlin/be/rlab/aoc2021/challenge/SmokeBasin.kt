package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.SquareGrid
import be.rlab.aoc2021.support.ResourceUtils.loadInput
import be.rlab.aoc2021.support.SearchUtils.breadthFirstSearch
import be.rlab.aoc2021.support.Tile

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
    val tile: Tile
)

class HeightMap(
    width: Int,
    height: Int,
    private val terrains: List<Terrain>
) : SquareGrid(width, height) {
    val lowPoints: List<Terrain> by lazy {
        terrains.filter { terrain ->
            val neighbors: List<Terrain> = findNeighbors(terrain)
            terrain.height < neighbors.minOfOrNull { it.height }!!
        }
    }

    fun basinSize(terrain: Terrain): Int {
        val visitedTerrains = breadthFirstSearch(
            this,
            start = terrain.tile,
            accepts = { tile -> terrains[tile.translateToIndex(width)].height < TERRAIN_PEEK }
        )
        return visitedTerrains.size
    }


    private fun findNeighbors(terrain: Terrain): List<Terrain> {
        return neighbors(terrain.tile).map { tile ->
            terrains[tile.translateToIndex(width)]
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()
    val lines = loadInput("09-smoke-basin.txt").split("\n")
    val width: Int = lines[0].length
    val heightMap = HeightMap(
        width = width,
        height = lines.size,
        terrains = lines.flatMap { line ->
            line.split("").filter { it.isNotEmpty() }.map { digit -> digit.toInt() }
        }.mapIndexed { index, height ->
            val x: Int = index % width
            val y: Int = index / width
            Terrain(height, Tile(x, y))
        }
    )

    val totalLowPoints = heightMap.lowPoints.sumOf { terrain ->
        terrain.height + 1
    }
    val basinSizes = heightMap.lowPoints.map { terrain ->
        heightMap.basinSize(terrain)
    }.sortedDescending()
    val largestBasins = basinSizes[0] * basinSizes[1] * basinSizes[2]

    assert(totalLowPoints == 417)
    assert(largestBasins == 1148965)

    println("total low points: $totalLowPoints")
    println("largest basins: $largestBasins")
    println("${System.currentTimeMillis() - start}ms")
}
