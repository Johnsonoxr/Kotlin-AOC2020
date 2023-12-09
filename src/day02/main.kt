package day02

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day02"

fun main() {
    fun part1(input: List<String>): Int {
        return input.count { line ->
            val (from, to, char, password) = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex().find(line)!!.destructured
            return@count password.count { it == char[0] } in from.toInt()..to.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { line ->
            val (idx1, idx2, char, password) = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex().find(line)!!.destructured
            return@count (password[idx1.toInt() - 1] == char[0]) xor (password[idx2.toInt() - 1] == char[0])
        }
    }

    check(part1(readInput("$FOLDER/test")) == 2)
    check(part2(readInput("$FOLDER/test")) == 1)

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