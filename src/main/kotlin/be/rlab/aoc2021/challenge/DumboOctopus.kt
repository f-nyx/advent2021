package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.Point.Companion.neighbors
import be.rlab.aoc2021.support.Point as PlanePoint
import be.rlab.aoc2021.support.ResourceUtils

/** Day 11: Dumbo Octopus
 *
 * This challenge is a simulation. We need to find different moments when a group of
 * dumbo octopuses flash (emits light).
 *
 * This simulation is very similar to the Smoke Basin (day 9) challenge. In each generation we need to increase
 * the energy level of all octopuses by 1. An octopus flashes when it reaches an energy level of 9. When it flashes,
 * all neighbor octopuses increase the energy level by 1. At the end of the generation, we reset the energy
 * level of all the octopuses that flashed back to 0.
 *
 * This solution uses a List to store the octopus map (the position of each octopus in a plane).
 * It increases the energy level for each individual octopus, and if it flashes, it repeats the
 * process for each neighbor. It uses a Set to store the octopuses that already flashed, since
 * an octopus can flash only once per generation.
 *
 * In the first part we need to count how many octopuses flashed in 100 generations.
 * In the second part we need to search the generation in which all octopuses flash at the same time.
 */

const val MAX_ENERGY_LEVEL = 9

data class Octopus(
    val index: Int,
    var energyLevel: Int
) {
    fun inc(): Boolean {
        if (energyLevel == MAX_ENERGY_LEVEL) {
            energyLevel = 0
        } else {
            energyLevel += 1
        }
        return energyLevel == 0
    }

    fun reset(): Octopus = apply {
        energyLevel = 0
    }
}

data class OctopusMap(
    private val width: Int,
    private val height: Int,
    private val octopuses: List<Octopus>
) {
    companion object {
        fun from(lines: List<String>): OctopusMap = OctopusMap(
            width = lines[0].length,
            height = lines.size,
            octopuses = lines.flatMap { line ->
                line.split("").filter { it.isNotEmpty() }.map { digit -> digit.toInt() }
            }.mapIndexed { index, energyLevel -> Octopus(index, energyLevel) }
        )
    }

    fun nextStep(): Int {
        return octopuses.fold(mutableSetOf<Octopus>()) { flashing, octopus ->
            process(octopus, flashing)
        }.map(Octopus::reset).size
    }

    fun firstSynchronized(): Int {
        var step = 1
        while (nextStep() != octopuses.size) {
            step++
        }
        return step
    }

    private fun process(
        octopus: Octopus,
        flashing: MutableSet<Octopus>
    ): MutableSet<Octopus> {
        if (octopus.inc()) {
            val mustFlash: Boolean = !flashing.contains(octopus)
            flashing += octopus
            if (mustFlash) {
                findNeighbors(octopus).forEach { neighbor -> process(neighbor, flashing) }
            }
        }
        return flashing
    }

    private fun findNeighbors(octopus: Octopus): List<Octopus> {
        return neighbors(
            point = PlanePoint(width, height, octopus.index),
            vertices = true
        ).map { neighbor ->
            octopuses[neighbor.index]
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()
    val lines: List<String> = ResourceUtils.loadInput("11-dumbo-octopus.txt").split("\n")

    // Part 1
    val map = OctopusMap.from(lines)
    val flashingCount = (0 until 100).sumOf { map.nextStep() }
    assert(flashingCount == 1713)
    println("flashing at 100 steps: $flashingCount")

    // Part 2
    val idealMap = OctopusMap.from(lines)
    val syncStep = idealMap.firstSynchronized()
    assert(flashingCount == 502)
    println("first synchronized step: $syncStep")
    println("${System.currentTimeMillis() - start}ms")
}
