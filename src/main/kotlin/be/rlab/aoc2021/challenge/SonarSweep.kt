package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

/** Day 1: Sonar Sweep
 *
 * This challenge is about counting _increments_ in a list of numbers. But instead of making it
 * easy, it requires counting increments using a _window_. Let's say we have a window of three numbers:
 *
 * Step 1: [1 2 3] 4 5 6 = 6
 * Step 2: 1 [2 3 4] 5 6 = 9
 * Step 3: 1 2 [3 4 5] 6 = 12
 * Step 4: 1 2 3 [4 5 6] = 15
 * Step 5: 1 2 3 4 [5 6] = 11
 *
 * The window value is the sum of all numbers in the window. In this example, the window value
 * increases from steps 1 to 4, so we have 4 _increments_.
 *
 * The challenge has two parts, the first part is counting increments with a window of one, and the
 * second part is counting increments with a window of three.
 */

fun countIncrements(depths: List<Int>): Int {
    return depths.foldIndexed(0) { index, increments, depth ->
        if (index > 0 && depth > depths[index - 1]) {
            increments + 1
        } else {
            increments
        }
    }
}

fun calculateDepthsInWindow(
    depths: List<Int>,
    windowSize: Int
): List<Int> {
    val slicingWindow: IntArray = (0 until windowSize).map { 0 }.toIntArray()

    return depths.foldIndexed(emptyList()) { depthIndex, depthsInWindow, _ ->
        if (depthIndex <= depths.size - windowSize) {
            (0 until windowSize).forEach { slotIndex ->
                slicingWindow[slotIndex] = depths[depthIndex + slotIndex]
            }
            depthsInWindow + slicingWindow.fold(0) { total, depth -> total + depth }
        } else {
            depthsInWindow
        }
    }
}

fun main() {
    val depths: List<Int> = loadInput("01-sonar_sweep_input.txt").split("\n").map(String::toInt)
    val depthIncrements: Int = countIncrements(depths)
    val depthIncrementsInWindow: Int = countIncrements(calculateDepthsInWindow(depths, 3))
    println("Depth increments: $depthIncrements")
    println("Depth increments in window: $depthIncrementsInWindow")
}
