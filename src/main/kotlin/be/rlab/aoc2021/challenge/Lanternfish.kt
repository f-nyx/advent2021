package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

/** Day 6: Lanternfish
 *
 * This challenge is about running a simulation. Lanternfishes will spawn new offsprings every N days.
 * We need to run the simulation and calculate the total amount of fishes after N days.
 *
 * The algorithm is really simple, the key part of this challenge is performance. Running the
 * simulation 250 times will return 1640526601595 results for this puzzle input. It means it's
 * not possible to calculate the result just with processing power.
 *
 * The solution is _memoization_, a form of caching to store the results of a function based on
 * the parameters. This data set is very normalized, so we return the cached results for the same parameters.
 */

val cache: MutableMap<Int, MutableMap<Long, Long>> = mutableMapOf(
    0 to mutableMapOf(),
    1 to mutableMapOf(),
    2 to mutableMapOf(),
    3 to mutableMapOf(),
    4 to mutableMapOf(),
    5 to mutableMapOf(),
    6 to mutableMapOf(),
    7 to mutableMapOf(),
    8 to mutableMapOf()
)

fun projectFishLife(
    daysForNextGen: Int,
    remainingDays: Long
): Long {
    val nextGenTime = remainingDays - (daysForNextGen + 1)
    val daysCache = cache[daysForNextGen] ?: mutableMapOf()

    return when {
        remainingDays == 0L || nextGenTime < 0 -> 0
        remainingDays in daysCache -> daysCache.getValue(remainingDays)
        else -> {
            daysCache[remainingDays] = projectFishLife(6, nextGenTime) + projectFishLife(8, nextGenTime) + 1
            daysCache.getValue(remainingDays)
        }
    }
}

fun countGenerations(
    timesToSpawnNextGen: List<Int>,
    totalDays: Int
): Long {
    return timesToSpawnNextGen.fold(0L) { result, fishTimeToSpawn ->
        result + projectFishLife(fishTimeToSpawn, totalDays.toLong()) + 1
    }
}

fun main() {
    val fishes: List<Int> = loadInput("06-lanternfish.txt").split(",").map { it.toInt() }
    println(countGenerations(fishes, 80))
    println(countGenerations(fishes, 256))
}
