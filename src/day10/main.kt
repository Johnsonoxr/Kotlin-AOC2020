package day10

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day10"

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.toInt() }
            .let { it + listOf(0, it.max() + 3) }
            .sorted()
            .windowed(2)
            .map { (jolt1, jolt2) -> jolt2 - jolt1 }
            .groupBy { it }
            .let { it[3]!!.size * it[1]!!.size }
    }

    fun part2(input: List<String>): Long {

        val countCache = mutableMapOf<List<Int>, Long>()

        fun countArrangements(joltOffsets: List<Int>): Long {
            if (joltOffsets.size <= 1) return 1
            return countCache.getOrPut(joltOffsets) {
                val drop1Count = countArrangements(joltOffsets.drop(1))
                when (joltOffsets.take(2).sum()) {
                    2 -> drop1Count + countArrangements(listOf(2) + joltOffsets.drop(2))
                    3 -> drop1Count + countArrangements(joltOffsets.drop(2))
                    else -> drop1Count
                }
            }
        }

        val joltOffsets = input
            .map { it.toInt() }
            .let { it + listOf(0, it.max() + 3) }
            .sorted()
            .windowed(2)
            .map { (jolt1, jolt2) -> jolt2 - jolt1 }

        return countArrangements(joltOffsets).also { it.println() }
    }

    check(part1(readInput("$FOLDER/test")) == 220)
    check(part2(readInput("$FOLDER/test")) == 19208L)

    val input = readInput("$FOLDER/input")
    val part1Result: Int
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