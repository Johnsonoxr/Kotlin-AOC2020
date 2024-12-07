package day08

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day08"

fun main() {

    data class Operation(val name: String, val value: Int)

    fun parseOperations(inputs: List<String>): List<Operation> {
        return inputs.map { line ->
            val (op, nStr) = line.split(" ")
            Operation(op, nStr.toInt())
        }
    }


    fun part1(input: List<String>): Int {
        val operations = parseOperations(input)
        val stepIndices = mutableListOf<Int>()
        var currentAcc = 0
        var currentOperationIndex = 0
        while (currentOperationIndex !in stepIndices) {
            stepIndices += currentOperationIndex
            val operation = operations[currentOperationIndex]
            currentAcc += when (operation.name) {
                "acc" -> operation.value
                else -> 0
            }
            currentOperationIndex += when (operation.name) {
                "jmp" -> operation.value
                else -> 1
            }
            currentOperationIndex = ((currentOperationIndex % operations.size) + operations.size) % operations.size
        }

        return currentAcc
    }

    fun part2(input: List<String>): Int {
        val operations = parseOperations(input)

        operations.forEachIndexed { opIdx, op ->
            val newOp = when (op.name) {
                "nop" -> Operation("jmp", op.value)
                "jmp" -> Operation("nop", op.value)
                else -> return@forEachIndexed
            }
            val newOperations = operations.toMutableList().apply { set(opIdx, newOp) }

            val stepIndices = mutableListOf<Int>()
            var currentAcc = 0
            var currentOperationIndex = 0
            while (currentOperationIndex !in stepIndices) {
                stepIndices += currentOperationIndex
                val operation = newOperations[currentOperationIndex]
                currentAcc += when (operation.name) {
                    "acc" -> operation.value
                    else -> 0
                }

                if (currentOperationIndex == newOperations.size - 1) {
                    return currentAcc
                }

                currentOperationIndex += when (operation.name) {
                    "jmp" -> operation.value
                    else -> 1
                }
                currentOperationIndex = ((currentOperationIndex % newOperations.size) + newOperations.size) % newOperations.size
            }
        }

        return -1
    }

    check(part1(readInput("$FOLDER/test")) == 5)
    check(part2(readInput("$FOLDER/test")) == 8)

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