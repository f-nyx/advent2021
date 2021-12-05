package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

/** Day 2: Dive!
 *
 * This challenge is a sort of state machine. You have to move your Submarine in three directions:
 * forward, down, and up.
 *
 * In this solution the Submarine runs the program provided by the puzzle input. Each
 * operation to move the submarine performs the changes in the internal parameters:
 * depth, positionX and aim.
 */

class Submarine(
    var depth: Int,
    var positionX: Int,
    var aim: Int
) {
    fun run(commands: List<Command>): Int {
        commands.forEach { command -> command(this) }
        return positionX * depth
    }
}

typealias Command = (Submarine) -> Unit

fun forward(argument: Int): Command = { submarine ->
    submarine.positionX += argument
    submarine.depth += submarine.aim * argument
}

fun down(argument: Int): Command = { submarine ->
    submarine.aim += argument
}

fun up(argument: Int): Command = { submarine ->
    submarine.aim -= argument
}

fun main() {
    val commands: List<Command> = loadInput("02-dive.txt").split("\n").map { commandString ->
        val (command, argument) = commandString.trim().split(" ")
        when (command.lowercase()) {
            "forward" -> forward(argument.toInt())
            "down" -> down(argument.toInt())
            "up" -> up(argument.toInt())
            else -> throw RuntimeException("command not supported: $commandString")
        }
    }
    val submarine = Submarine(0, 0, 0)
    println(submarine.run(commands))
}
