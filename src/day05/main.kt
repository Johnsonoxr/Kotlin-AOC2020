package day05

import readInput
import kotlin.math.pow
import kotlin.system.measureNanoTime

private const val FOLDER = "day05"

fun main() {

    fun calcSeatId(boardingPass: String): Int {
        val bfList = boardingPass.take(7)
        val rlList = boardingPass.takeLast(3)
        val row = bfList.reversed().mapIndexed { index, c -> 2.0.pow(index).toInt() * if (c == 'B') 1 else 0 }.sum()
        val column = rlList.reversed().mapIndexed { index, c -> 2.0.pow(index).toInt() * if (c == 'R') 1 else 0 }.sum()
        return row * 8 + column
    }

    fun part1(input: List<String>): Int {
        return input.maxOf { calcSeatId(it) }
    }

    fun part2(input: List<String>): Int {
        val ids = input.map { calcSeatId(it) }.sorted()
        var prevId: Int? = null
        for (id in ids) {
            prevId = when (prevId) {
                null -> id
                id - 1 -> id
                else -> break
            }
        }
        return prevId!! + 1
    }

    check(part1(readInput("$FOLDER/test")) == 820)
//    check(part2(readInput("$FOLDER/test")) == 1)

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