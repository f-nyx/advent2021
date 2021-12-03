package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils.loadInput

data class BinaryCounter(
    val zeros: IntArray,
    val ones: IntArray
) {
    companion object {
        fun new(size: Int): BinaryCounter = BinaryCounter(
            zeros = "0".repeat(size).map(Char::digitToInt).toIntArray(),
            ones = "0".repeat(size).map(Char::digitToInt).toIntArray()
        )
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
    val counter = countBits(report)
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
        val counter = countBits(samples)
        if (samples.size > 1) {
            val bitValue = filterBit(counter.zeros[bitPosition], counter.ones[bitPosition])
            samples.removeIf { line ->
                line[bitPosition].digitToInt() == bitValue
            }
        }
    }
    return samples.first().toInt(2)
}

fun countBits(binaryNumbers: List<String>): BinaryCounter {
    return binaryNumbers.fold(
        BinaryCounter.new(binaryNumbers.first().length)
    ) { counter, binaryNumber ->
        binaryNumber.forEachIndexed { bitPosition, char ->
            counter.increment(bitPosition, char.digitToInt())
        }
        counter
    }
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
