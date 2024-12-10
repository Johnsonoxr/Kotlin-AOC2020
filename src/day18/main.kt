package day18

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day18"

fun main() {
    fun part1(input: List<String>): Long {

        fun runMath(mathExpression: String): String {
            mathExpression.toLongOrNull()?.let { return mathExpression }
            val exp = Regex("\\(\\d+ [+*] \\d+\\)").find(mathExpression)
                ?: Regex("\\d+ [+*] \\d+").find(mathExpression)
                ?: throw IllegalArgumentException("math expression analyze failed. $mathExpression")
            val (a, op, b) = exp.value.trim('(', ')').split(" ")
            val aOpB = when (op) {
                "+" -> a.toLong() + b.toLong()
                "*" -> a.toLong() * b.toLong()
                else -> throw IllegalArgumentException("Unexpected operator $op")
            }
            return runMath(mathExpression.replaceRange(exp.range, aOpB.toString()))
        }

        return input.sumOf { line -> runMath(line).toLong() }
    }

    fun part2(input: List<String>): Long {

        fun runMath(mathExpression: String): String {
            mathExpression.toLongOrNull()?.let { return mathExpression }

            var mathExpWithoutBrackets = mathExpression
            while (true) {
                val brackets = Regex("\\([\\d+* ]+\\)").find(mathExpWithoutBrackets) ?: break
                mathExpWithoutBrackets = mathExpWithoutBrackets.replaceRange(brackets.range, runMath(brackets.value.drop(1).dropLast(1)))
            }

            val exp = Regex("\\d+ \\+ \\d+").find(mathExpWithoutBrackets)
                ?: Regex("\\d+ \\* \\d+").find(mathExpWithoutBrackets)
                ?: throw IllegalArgumentException("math expression analyze failed. $mathExpWithoutBrackets")
            val (a, op, b) = exp.value.split(" ")
            val aOpB = when (op) {
                "+" -> a.toLong() + b.toLong()
                "*" -> a.toLong() * b.toLong()
                else -> throw IllegalArgumentException("Unexpected operator $op")
            }
            return runMath(mathExpWithoutBrackets.replaceRange(exp.range, aOpB.toString()))
        }

        return input.sumOf { line -> runMath(line).toLong() }
    }

    check(part1(readInput("$FOLDER/test")) == 26457L)
    check(part2(readInput("$FOLDER/test")) == 694173L)

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