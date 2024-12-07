package day06

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day06"

fun main() {

    fun toGroupedAnswers(input: List<String>): List<List<Set<Char>>> {
        val groups = mutableListOf<MutableList<Set<Char>>>(mutableListOf())
        input.forEach { line ->
            if (line.isBlank()) {
                groups.add(mutableListOf())
            } else {
                groups.last().add(line.toSet())
            }
        }
        return groups
    }

    fun part1(input: List<String>): Int {
        return toGroupedAnswers(input).sumOf { group -> group.flatten().toSet().size }
    }

    fun part2(input: List<String>): Int {
        return toGroupedAnswers(input).sumOf { group -> group.flatten().groupBy { it }.filter { it.value.size == group.size }.size }
    }

    check(part1(readInput("$FOLDER/test")) == 11)
    check(part2(readInput("$FOLDER/test")) == 6)

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