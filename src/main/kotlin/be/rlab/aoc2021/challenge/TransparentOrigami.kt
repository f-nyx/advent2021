package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils
import be.rlab.aoc2021.support.Tile

/** Day 13: Transparent Origami
 *
 * This challenge is about matrix translation. There are a series of dots (points) on a transparent paper (plane).
 * We need to fold the paper vertically and horizontally according to a list of instructions, and in the
 * end the overlapped dots will provide a code in ascii art.
 *
 * This algorithm calculates the range of the points to translate according to each instruction,
 * and then it translates each individual point in the range to the folded position. After the translation,
 * it removes all points beyond the instruction line. Finally, it removes duplicated points. We have duplicated
 * dots because some of the points might end up overlapping after the fold is complete.
 */


/** Represents a single folding instruction.
 */
data class FoldingInstruction(
    val lineValue: Int,
    val vertical: Boolean
) {
    fun axisOf(point: Tile): Int {
        return if (vertical) {
            point.y
        } else {
            point.x
        }
    }

    fun translate(point: Tile): Tile {
        val translatedAxis = lineValue - (axisOf(point) - lineValue)
        return if (vertical) {
            point.copy(y = translatedAxis)
        } else {
            point.copy(x = translatedAxis)
        }
    }
}

class TransparentPaper(
    private val width: Int,
    private val height: Int,
    private var points: MutableList<Tile>,
    private val instructions: MutableList<FoldingInstruction>
) {
    companion object {
        fun new(lines: List<String>): TransparentPaper {
            val points = lines.filter { line -> "," in line }.map { line ->
                val (x, y) = line.split(",")
                Tile(x.toInt(), y.toInt())
            }
            val instructions = lines.filter { line -> "fold along" in line }.map { line ->
                val (axis, value) = line.substringAfter("fold along ").split("=")
                FoldingInstruction(lineValue = value.toInt(), vertical = axis == "y")
            }
            return TransparentPaper(
                width = points.maxOf { it.x } + 1,
                height = points.maxOf { it.y } + 1,
                points.toMutableList(),
                instructions.toMutableList()
            )
        }
    }

    fun foldNext(): Int {
        fold(instructions.removeFirst())
        return points.size
    }

    fun foldAll() {
        while (instructions.isNotEmpty()) {
            fold(instructions.removeFirst())
        }
    }

    private fun fold(instruction: FoldingInstruction) {
        // Range of values within the instruction axis that will be translated.
        val range = instruction.lineValue .. (instruction.lineValue * 2)

        // Translates all points in the range to the folding position.
        points.filter { point ->
            instruction.axisOf(point) in range
        }.forEach { point ->
            val index = points.indexOf(point)
            points[index] = instruction.translate(point)
        }
        // Removes all points that are out of range.
        points.removeIf { point -> instruction.axisOf(point) > instruction.lineValue }
        // Removes overlapping points.
        points = points.distinct().toMutableList()
    }

    fun joinToString(): String {
        val size = width * height
        return MutableList(size) { "." }.mapIndexed { index, tile ->
            val x = index % width
            val y = index / width

            if (Tile(x, y) in points) {
                "#"
            } else {
                tile
            }
        }.joinToString("").chunked(width).joinToString("\n")
    }
}

fun main() {
    val start = System.currentTimeMillis()
    val lines: List<String> = ResourceUtils.loadInput("13-transparent-origami.txt").split("\n")
    val paper = TransparentPaper.new(lines)

    // Part 1
    val numberOfPoints = paper.foldNext()
    assert(numberOfPoints == 708)
    println("Number of points after first folding: $numberOfPoints}")

    // Part 2
    paper.foldAll()
    println(paper.joinToString())
    println("${System.currentTimeMillis() - start}ms")
}
