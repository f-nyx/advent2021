package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

/** Day 4 - Giant Squid
 *
 * This solution uses a single list to store all boards. Each board is a square with 25 Slots (5 columns x 5 rows),
 * and each Slot contains the number and the state (marked or unmarked). By default, all slots are created
 * with the state _unmarked_.
 *
 * In order to identify components in the list (boards, columns, rows, etc) it uses some simple geometry math.
 * This algorithm can identify components from an element in the slots list, using the element index. Look at
 * the comments below to check the operations for resolving each component.
 */

private const val BOARD_WIDTH = 5
private const val BOARD_HEIGHT = 5
private const val BOARD_SIZE = BOARD_WIDTH * BOARD_HEIGHT

data class Slot(
    val number: Int,
    var marked: Boolean
)

fun parseBoards(lines: List<String>): List<Slot> {
    return lines.takeLast(lines.size - 1).flatMap { line ->
        if (line.isNotEmpty()) {
            line.split(" ")
                .filter { number -> number.isNotEmpty() }
                .map { number -> Slot(number = number.trim().toInt(), marked = false) }
        } else {
            emptyList()
        }
    }
}

fun main() {
    val lines: List<String> = loadInput("04-bingo.txt").split("\n")
    val numbers: List<Int> = lines[0].split(",").map { number -> number.trim().toInt() }
    val slots = parseBoards(lines)
    val boards: MutableList<Int> = mutableListOf()
    val results: MutableList<Int> = mutableListOf()

    numbers.forEach { number ->
        slots.forEachIndexed { slotIndex, slot ->
            if (slot.number == number) {
                slot.marked = true

                // Identifies a single board among all the boards, being 0 the first board in the puzzle input.
                val boardIndex = slotIndex / BOARD_SIZE
                // Index in the list of the first slot in the board.
                val boardStart = boardIndex * BOARD_SIZE
                // Id of the slot within the board, from 0 to 25, from left to right, being 0 in the
                // top-left corner of the board.
                val slotId = slotIndex - boardStart
                // 0-based column and row indexes for a single slot in the board.
                val columnIndex = slotId % BOARD_WIDTH
                val rowIndex = slotId / BOARD_WIDTH
                // All elements in a row.
                val row = slots.subList(
                    // Multiplying the row index by the board with will give you always the index
                    // of the first element in the row. In this case boardStart is just an offset.
                    boardStart + rowIndex * BOARD_WIDTH,
                    // Same as the previous comment, but the end of the row is the board width.
                    boardStart + rowIndex * BOARD_WIDTH + BOARD_WIDTH
                )
                // All elements in a column. Elements in a column are not contiguous as it happens in a row,
                // so we need to calculate each index. We need to traverse the board from top to bottom to
                // calculate each index.
                val column = (0 until BOARD_HEIGHT).map { rowIndex2 ->
                    // It uses the same formula to calculate row's first element,
                    // and then it adds the column index as an offset.
                    slots[boardStart + rowIndex2 * BOARD_WIDTH + columnIndex]
                }
                val rowBingo = row.all { it.marked }
                val columnBingo = column.all { it.marked }

                if (rowBingo || columnBingo) {
                    val board = slots.subList(boardStart, boardStart + BOARD_SIZE)
                    val unmarked = board.filter { !it.marked }.sumOf { it.number }

                    if (!boards.contains(boardIndex)) {
                        boards += boardIndex
                        results += unmarked * number
                    }
                }
            }
        }
    }
    println("First part: ${results.first()}")
    println("Second part: ${results.last()}")
}
