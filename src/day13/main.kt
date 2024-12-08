package day13

import readInput
import kotlin.math.ceil
import kotlin.system.measureNanoTime

private const val FOLDER = "day13"

fun main() {
    fun part1(input: List<String>): Int {
        val departTimestamp = input.first().toInt()
        val busList = input[1].split(",").filter { it != "x" }.map { it.toInt() }
        val closestBus = busList.minBy { bus -> ceil(departTimestamp / bus.toFloat()).toInt() * bus - departTimestamp }
        return (closestBus * ceil(departTimestamp / closestBus.toFloat()).toInt() - departTimestamp) * closestBus
    }

    fun part2(input: List<String>): Long {

        fun update(f1: Long, offset1: Long, f2: Long, offset2: Long): Pair<Long, Long> {
            (1..f2).forEach { f ->
                val v = f1 * f + offset1
                if ((v - offset2) % f2 == 0L) {
                    return f1 * f2 to v - f1 * f2
                }
            }
            throw IllegalArgumentException("Not gonna happen.")
        }

        val busList = input[1].split(",").map { it.toIntOrNull() }
        val busOffsetPairs = busList.mapIndexed { index, i -> if (i != null) i.toLong() to -index.toLong() else null }.filterNotNull()

        var (currentF, currentOffset) = busOffsetPairs.first()
        busOffsetPairs.drop(1).forEach { (f, offset) ->
            val result = update(currentF, currentOffset, f, offset)
            currentF = result.first
            currentOffset = result.second
        }

        return currentF + currentOffset
    }

    check(part1(readInput("$FOLDER/test")) == 295)
    check(part2(readInput("$FOLDER/test")) == 1068781L)

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