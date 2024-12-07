package day17

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day17"

fun main() {

    fun parseNumbers(input: List<String>) = input.map { it.toLong() }

    fun part1(input: List<String>, window: Int = 25): Long {
        parseNumbers(input).windowed(window + 1).forEach { numbers ->
            val target = numbers.last()
            val sources = numbers.dropLast(1)
            sources.forEachIndexed { index, i ->
                if (sources.subList(index + 1, sources.size).any { it + i == target }) {
                    return@forEach
                }
            }
            return target
        }
        throw IllegalArgumentException("No encryption weakness found.")
    }

    fun part2(input: List<String>, window: Int = 25): Long {
        val invalidNumber = part1(input, window)
        val numbers = parseNumbers(input)
        numbers.indices.forEach { index ->
            (2..numbers.size - index - 2).forEach { window ->
                val windowedNumbers = numbers.subList(index, index + window)
                if (windowedNumbers.sum() == invalidNumber) {
                    return windowedNumbers.max() + windowedNumbers.min()
                }
            }
        }
        throw IllegalArgumentException("No encryption weakness found.")
    }

    check(part1(readInput("$FOLDER/test"), 5) == 127L)
    check(part2(readInput("$FOLDER/test"), 5) == 62L)

    val input = readInput("$FOLDER/input")
    val part1Result: Long
    val part1Time = measureNanoTime {
        part1Result = part1(input)
    }
    val part2Result: Long
    val part2Time = measureNanoTime {
        part2Result = part2(input)
    }

    println("Part 1 result: $part1Result")
    println("Part 2 result: $part2Result")
    println("Part 1 takes ${part1Time / 1e6f} milliseconds.")
    println("Part 2 takes ${part2Time / 1e6f} milliseconds.")
}