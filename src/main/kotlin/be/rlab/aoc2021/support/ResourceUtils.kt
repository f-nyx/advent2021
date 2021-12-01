package be.rlab.aoc2021.support

object ResourceUtils {
    fun loadInput(name: String): String {
        return Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("input/$name")?.bufferedReader()?.readText()
            ?: throw RuntimeException("Cannot load input: $name")
    }
}
