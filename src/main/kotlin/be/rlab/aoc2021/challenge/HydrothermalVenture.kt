package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput
import kotlin.math.*

/** Day 5: Hydrothermal Venture
 *
 * This challenge is about counting intersections of lines in a plane.
 *
 * As lines only can be horizontal, vertical or at 45°, we know that the slope on each coordinate
 * can be either 1, -1 or 0. So if we know the length of the line and the slope of each coordinate, we
 * can calculate each point in the line.
 *
 * This algorithm calculates each point in every line and marks the point as _intersected_ if at least
 * two lines share the same point.
 */

val linesParser = "^(\\d+),(\\d+) -> (\\d+),(\\d+)$".toRegex()

data class Point(
    val x: Int,
    val y: Int
)
data class Line(
    val start: Point,
    val end: Point
)

fun countIntersections(lines: List<Line>): Int {
    val pointIntersections: Map<Point, Int> = lines.fold(mutableMapOf()) { results, line ->
        val slopeX: Int = when {
            line.start.x == line.end.x -> 0
            line.start.x < line.end.x -> 1
            else -> -1
        }
        val slopeY: Int = when {
            line.start.y == line.end.y -> 0
            line.start.y < line.end.y -> 1
            else -> -1
        }
        val length = max(
            abs(min(line.start.x, line.end.x) - max(line.start.x, line.end.x)),
            abs(min(line.start.y, line.end.y) - max(line.start.y, line.end.y))
        )
        var pointX = line.start.x
        var pointY = line.start.y

        repeat(length + 1) {
            val point = Point(pointX, pointY)
            if (!results.containsKey(point) || results.getValue(point) < 2) {
                results[point] = (results[point] ?: 0) + 1
            }
            pointX += slopeX
            pointY += slopeY
        }
        results
    }
    return pointIntersections.count { (_, value) -> value > 1 }
}

fun main() {
    val startTime = System.currentTimeMillis()
    val lines: List<Line> = loadInput("05-hydrothermal-map.txt").split("\n").map { line ->
        val values = linesParser.find(line)?.groupValues ?: throw RuntimeException("invalid input: $line")
        Line(
            start = Point(values[1].toInt(), values[2].toInt()),
            end = Point(values[3].toInt(), values[4].toInt())
        )
    }
    val rectLines = lines.filter { line ->
        line.start.x == line.end.x || line.start.y == line.end.y
    }
    println("rect lines intersections: ${countIntersections(rectLines)}")
    println("all lines intersections: ${countIntersections(lines)}")
    println("took ${System.currentTimeMillis() - startTime}ms")
}
