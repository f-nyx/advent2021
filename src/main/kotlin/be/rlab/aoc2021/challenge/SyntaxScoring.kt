package be.rlab.aoc2021.challenge

import be.rlab.aoc2021.support.ResourceUtils

/** Day 10: Syntax Scoring
 *
 * This challenge is about parsing a regular language.
 *
 * The solution uses a classic parsing strategy. Symbols have opening and closing tokens. The parser
 * pushes the symbol to a stack when it detects an opening token, and it removes the symbol from the stack
 * when it finds a closing token for the same symbol.
 *
 * For the first part, a syntax is invalid when a closing token is not from the symbol at the head of the stack.
 *
 * For the second part, we discard invalid lines and once a valid line is parsed, the stack will still contain
 * the symbols that need to be closed.
 */

sealed class Symbol(
    val opening: Char,
    val closing: Char,
    val parsingScore: Int,
    val autoCompleteScore: Int
) {
    object Bracket : Symbol('{', '}', parsingScore = 1197, autoCompleteScore = 3)
    object SquareBracket : Symbol('[', ']', parsingScore = 57, autoCompleteScore = 2)
    object AngleBracket : Symbol('<', '>', parsingScore = 25137, autoCompleteScore = 4)
    object Parentheses : Symbol('(', ')', parsingScore = 3, autoCompleteScore = 1)

    companion object {
        fun from(token: Char): Symbol {
            return listOf(Bracket, SquareBracket, AngleBracket, Parentheses).find { symbol ->
                symbol.opening == token || symbol.closing == token
            } ?: throw RuntimeException("syntax error, symbol not found for token: $token")
        }
    }
}

class Parser {
    private val symbols: ArrayDeque<Symbol> = ArrayDeque()

    /** Parses a line and returns the first invalid symbol, if any.
     * @param line Line to parse.
     * @return the first invalid symbol, if any.
     */
    fun parse(line: String): Symbol? {
        val invalidSymbolIndex = line.indexOfFirst { token ->
            val symbol = Symbol.from(token)

            if (symbol.opening == token) {
                symbols.addFirst(symbol)
                false
            } else {
                if (symbols.first() != symbol) {
                    true
                } else {
                    symbols.removeFirst()
                    false
                }
            }
        }
        return if (invalidSymbolIndex > -1)
            Symbol.from(line[invalidSymbolIndex])
        else
            null
    }

    /** Autocompletes missing closing symbols in a line.
     * @param line Line to autocomplete.
     * @return the score, or null if the line is invalid.
     */
    fun autoComplete(line: String): Long? {
        if (parse(line) != null) {
            return null
        }
        return symbols.fold(0L) { score, symbol ->
            score * 5 + symbol.autoCompleteScore
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()
    val lines: List<String> = ResourceUtils.loadInput("10-syntax-scoring.txt").split("\n")

    val parsingScore = lines
        .asSequence()
        .map { line -> Parser().parse(line) }
        .filterNotNull()
        .groupBy { symbol -> symbol }
        .map { (symbol, values) -> symbol.parsingScore * values.size }
        .sum()

    val autoCompleteScores = lines
        .mapNotNull { line -> Parser().autoComplete(line) }
        .sorted()

    println("parsing score: $parsingScore")
    println("auto complete score: ${autoCompleteScores[autoCompleteScores.size / 2]}")
    println("${System.currentTimeMillis() - start}ms")
}
