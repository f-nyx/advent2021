package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput
import kotlin.math.abs

/** Day 7: The Treachery of Whales
 *
 * This challenge is quite simple, it's about arithmetic progression.
 * The input is a list of numbers, and you need to calculate the arithmetic series between
 * all of them.
 *
 * The result is the minimum value of all the arithmetic series, which represents the minimum fuel cost
 * for crabs submarines.
 *
 * The arithmetic series is the number n of terms being added, multiplying by the sum of the first and last
 * number in the progression, and dividing by 2. In our case: (distance * distance + distance) / 2, where
 * distance is the subtraction between two numbers.
 */

fun fuelCost(
    source: Int,
    target: Int,
    incremental: Boolean
): Int {
    val distance = abs(target - source)
    return if (incremental) {
        (distance * distance + distance) / 2
    } else {
        distance
    }
}

fun calculateFuelCosts(
    submarines: List<Int>,
    incrementalCost: Boolean
): List<Int> {
    val maxDistance: Int = submarines.maxOrNull()!!

    return (0..maxDistance).fold(listOf()) { distanceCache, targetDistance ->
        distanceCache + submarines.sumOf { sourceSubmarine ->
            fuelCost(sourceSubmarine, targetDistance, incremental = incrementalCost)
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()
    val submarines: List<Int> = loadInput("07-crabs-submarines.txt").split(",").map { it.toInt() }
    val fuelCosts: List<Int> = calculateFuelCosts(submarines, incrementalCost = false)
    val incrementalFuelCosts: List<Int> = calculateFuelCosts(submarines, incrementalCost = true)
    println(fuelCosts.minOrNull())
    println(incrementalFuelCosts.minOrNull())
    println("${System.currentTimeMillis() - start}ms")
}
