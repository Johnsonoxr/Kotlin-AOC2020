package day01

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day01"

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { it.toInt() }

        numbers.forEach { n1 ->
            numbers.forEach { n2 ->
                if (n1 == n2) return@forEach
                if (n1 + n2 == 2020) {
                    return n1 * n2
                }
            }
        }

        throw IllegalStateException("No solution found")
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { it.toInt() }

        numbers.forEach { n1 ->
            numbers.forEach { n2 ->
                if (n1 == n2) return@forEach
                numbers.forEach { n3 ->
                    if (n1 == n3 || n2 == n3) return@forEach
                    if (n1 + n2 + n3 == 2020) {
                        return n1 * n2 * n3
                    }
                }
            }
        }

        throw IllegalStateException("No solution found")
    }

    check(part1(readInput("$FOLDER/test")) == 514579)
    check(part2(readInput("$FOLDER/test")) == 241861950)

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