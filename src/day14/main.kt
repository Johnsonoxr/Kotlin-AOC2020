package day14

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day14"

fun main() {
    fun part1(input: List<String>): Long {

        data class Write(val mem: Int, val dec: Int)

        var currentMask = input.first().removePrefix("mask = ")
        val memMap = mutableMapOf<Int, String>()

        input.drop(1).forEach { line ->
            when {
                line.startsWith("mask") -> {
                    currentMask = line.removePrefix("mask = ")
                }

                else -> {
                    val write = Regex("\\d+").findAll(line).map { it.value }.let { Write(it.first().toInt(), it.last().toInt()) }
                    val writeBinary = write.dec.toString(2).padStart(currentMask.length, '0')
                    val maskedBinary = writeBinary.zip(currentMask).map { (w, m) -> if (m == 'X') w else m }.joinToString("")
                    memMap[write.mem] = maskedBinary
                }
            }
        }

        return memMap.values.sumOf { it.toLong(2) }
    }

    fun part2(input: List<String>): Long {

        data class Write(val mem: Int, val dec: Int)

        var currentMask = input.first().removePrefix("mask = ")
        val memMap = mutableMapOf<Long, Long>()

        fun putMem(xMem: String, value: Long) {
            when {
                'X' !in xMem -> memMap[xMem.toLong(2)] = value
                else -> {
                    putMem(xMem.replaceFirst('X', '0'), value)
                    putMem(xMem.replaceFirst('X', '1'), value)
                }
            }
        }

        input.drop(1).forEach { line ->
            when {
                line.startsWith("mask") -> {
                    currentMask = line.removePrefix("mask = ")
                }

                else -> {
                    val write = Regex("\\d+").findAll(line).map { it.value }.let { Write(it.first().toInt(), it.last().toInt()) }
                    val memBinary = write.mem.toString(2).padStart(currentMask.length, '0')
                    val maskedMemBinary = memBinary.zip(currentMask).map { (mem, mask) -> if (mask == '0') mem else mask }.joinToString("")
                    putMem(maskedMemBinary, write.dec.toLong())
                }
            }
        }

        return memMap.values.sum()
    }

    check(part1(readInput("$FOLDER/test")) == 165L)
    check(part2(readInput("$FOLDER/test2")) == 208L)

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