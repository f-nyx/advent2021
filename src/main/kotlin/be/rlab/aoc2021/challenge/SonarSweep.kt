package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

const val INPUT_NAME: String = "01-sonar_sweep_input.txt"

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
    val depths: List<Int> = loadInput(INPUT_NAME).split("\n").map(String::toInt)
    val depthIncrements: Int = countIncrements(depths)
    val depthsInWindow: List<Int> = calculateDepthsInWindow(depths, 3)
    val depthIncrementsInWindow: Int = countIncrements(depthsInWindow)
    println("Depth increments: $depthIncrements")
    println("Depth increments in window: $depthIncrementsInWindow")
}
