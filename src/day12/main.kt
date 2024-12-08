package day12

import readInput
import kotlin.math.abs
import kotlin.system.measureNanoTime

private const val FOLDER = "day12"

fun main() {

    val directions = listOf('N', 'E', 'S', 'W')

    data class Operation(val name: Char, val value: Int)
    data class State(val direction: Char, val x: Int, val y: Int) {
        fun move(direction: Char, value: Int): State = when (direction) {
            'N' -> copy(y = y - value)
            'E' -> copy(x = x + value)
            'S' -> copy(y = y + value)
            'W' -> copy(x = x - value)
            else -> throw IllegalArgumentException("Illegal direction $direction")
        }

        fun turn(turn: Char, degree: Int): State {
            val nextDirectionIndex = when (turn) {
                'R' -> (directions.indexOf(direction) + degree / 90) % 4
                else -> (directions.indexOf(direction) - degree / 90 + 4) % 4
            }
            return copy(direction = directions[nextDirectionIndex])
        }
    }

    fun parseOperations(input: List<String>): List<Operation> {
        return input.map { line ->
            Operation(line.first(), line.drop(1).toInt())
        }
    }

    fun part1(input: List<String>): Int {
        val operations = parseOperations(input)
        var state = State('E', 0, 0)
        operations.forEach { op ->
            state = when (op.name) {
                in directions -> state.move(op.name, op.value)
                in listOf('L', 'R') -> state.turn(op.name, op.value)
                else -> state.move(state.direction, op.value)
            }
        }
        return abs(state.x) + abs(state.y)
    }

    fun part2(input: List<String>): Int {
        val operations = parseOperations(input)
        var waypoint = State('N', 10, -1)
        var shipState = State('N', 0, 0)

        operations.forEach { op ->
            when (op.name) {
                in directions -> {
                    waypoint = waypoint.move(op.name, op.value)
                }

                'L' -> {
                    repeat(op.value / 90) {
                        waypoint = waypoint.copy(x = waypoint.y, y = -waypoint.x)
                    }
                }

                'R' -> {
                    repeat(op.value / 90) {
                        waypoint = waypoint.copy(x = -waypoint.y, y = waypoint.x)
                    }
                }

                else -> {
                    shipState = shipState.move('S', waypoint.y * op.value).move('E', waypoint.x * op.value)
                }
            }
        }

        return abs(shipState.x) + abs(shipState.y)
    }

    check(part1(readInput("$FOLDER/test")) == 25)
    check(part2(readInput("$FOLDER/test")) == 286)

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