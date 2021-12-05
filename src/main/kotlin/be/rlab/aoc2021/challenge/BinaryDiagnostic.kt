package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

/** Day 3: Binary Diagnostic
 *
 * This challenge is about counting bits. Given a list of binary numbers, you need to take
 * decisions based on how often a bit (0 or 1) appear in a position.
 *
 * The solution is based on a [BinaryCounter], a component that simplifies counting bits.
 * For each binary number in the input, the binary counter will store how often a bit (0 or 1)
 * appears in a position. This relies on all numbers having the same length, which is true for
 * the puzzle input (12 bits).
 *
 * Once the BinaryCounter does its work, the rest of the solution is about comparing and filtering
 * the results.
 */


data class BinaryCounter(
    val zeros: IntArray,
    val ones: IntArray
) {
    companion object {
        fun countBits(binaryNumbers: List<String>): BinaryCounter {
            val size = binaryNumbers.first().length

            return binaryNumbers.fold(
                BinaryCounter(
                    zeros = "0".repeat(size).map(Char::digitToInt).toIntArray(),
                    ones = "0".repeat(size).map(Char::digitToInt).toIntArray()
                )
            ) { counter, binaryNumber ->
                binaryNumber.forEachIndexed { bitPosition, char ->
                    counter.increment(bitPosition, char.digitToInt())
                }
                counter
            }
        }
    }

    fun increment(
        bitPosition: Int,
        bitValue: Int
    ) {
        when (bitValue) {
            0 -> zeros[bitPosition] += 1
            1 -> ones[bitPosition] += 1
        }
    }
}

fun powerConsumptionRating(
    report: List<String>,
    resolveBit: (Int, Int) -> Int
): Int {
    val counter = BinaryCounter.countBits(report)
    val bitCount = report.first().length

    return (0 until bitCount).map { bitPosition ->
        resolveBit(counter.zeros[bitPosition], counter.ones[bitPosition])
    }.joinToString("").toInt(2)
}

fun lifeSupportRating(
    report: List<String>,
    filterBit: (Int, Int) -> Int
): Int {
    val samples = report.toMutableList()
    val bitCount = report.first().length

    (0 until bitCount).forEach { bitPosition ->
        val counter = BinaryCounter.countBits(samples)
        if (samples.size > 1) {
            val bitValue = filterBit(counter.zeros[bitPosition], counter.ones[bitPosition])
            samples.removeIf { line ->
                line[bitPosition].digitToInt() == bitValue
            }
        }
    }
    return samples.first().toInt(2)
}

fun main() {
    val report: MutableList<String> = loadInput("03-diagnostic_report.txt").split("\n").toMutableList()

    // Part 1
    val gamaRate: Int = powerConsumptionRating(report) { zeroCount, oneCount ->
        if (zeroCount > oneCount) {
            0
        } else {
            1
        }
    }
    val epsilonRate: Int = powerConsumptionRating(report) { zeroCount, oneCount ->
        if (zeroCount < oneCount) {
            0
        } else {
            1
        }
    }
    println("power consumption rating: ${gamaRate * epsilonRate}")

    // Part 2
    val oxygenGeneratorRating: Int = lifeSupportRating(report) { zeroCount, oneCount ->
        when {
            zeroCount > oneCount -> 1
            zeroCount == oneCount -> 0
            else -> 0
        }
    }
    val co2ScrubberRating: Int = lifeSupportRating(report) { zeroCount, oneCount ->
        when {
            zeroCount < oneCount -> 1
            zeroCount == oneCount -> 1
            else -> 0
        }
    }
    println("life support rating: ${oxygenGeneratorRating * co2ScrubberRating}")
}
