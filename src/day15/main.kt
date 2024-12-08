package day15

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day15"

fun main() {

    fun runGame(startNumbers: List<Int>, rounds: Int): Int {
        var next1 = startNumbers.last()
        val numberLocationMap =
            startNumbers.dropLast(1).mapIndexed { index, i -> index to i }.associate { (index, i) -> i to index + 1 }.toMutableMap()

        (numberLocationMap.size + 1..<rounds).forEach { round ->
            val next2 = when (val prevLocation = numberLocationMap[next1]) {
                null -> 0
                else -> round - prevLocation
            }
            numberLocationMap[next1] = round
            next1 = next2
        }

        return next1
    }

    fun part1(input: List<String>): Int {
        return runGame(input.first().split(",").map { it.toInt() }, 2020)
    }

    fun part2(input: List<String>): Int {
        return runGame(input.first().split(",").map { it.toInt() }, 30_000_000)
    }

    check(part1(readInput("$FOLDER/test")) == 436)
    check(part2(readInput("$FOLDER/test")) == 175594)

    val input = readInput("$FOLDER/input")
    val part1Result: Int
    val part1Time = measureNanoTime {
        part1Result = part1(input)
    }
    val part2Result: Int
    val part2Time = measureNanoTime {
        part2Result = part2(input)
    }

    println("Part 1 result: $part1Result")
    println("Part 2 result: $part2Result")
    println("Part 1 takes ${part1Time / 1e6f} milliseconds.")
    println("Part 2 takes ${part2Time / 1e6f} milliseconds.")
}