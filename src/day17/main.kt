package day17

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day17"

fun main() {

    data class Cube(val coordinate: List<Int>)

    fun parseCubes(input: List<String>, dimension: Int): Set<Cube> {
        return input.mapIndexed { y, line ->
            line.toList().mapIndexed { x, c ->
                when (c) {
                    '#' -> Cube(listOf(x, y) + List(dimension - 2) { 0 })
                    else -> null
                }
            }.filterNotNull()
        }.flatten().toSet()
    }

    fun Cube.getNeighborsAndSelf(axis: Int = 0): Set<Cube> {
        val neighborsAlongAxis = coordinate[axis].let { a -> (a - 1..a + 1).map { Cube(coordinate.toMutableList().apply { set(axis, it) }) } }
        return when {
            axis == coordinate.size - 1 -> neighborsAlongAxis
            else -> neighborsAlongAxis.map { it.getNeighborsAndSelf(axis + 1) }.flatten()
        }.toSet()
    }

    val cachedNeighbors = mutableMapOf<Cube, Set<Cube>>()
    fun Cube.getNeighbors(): Set<Cube> {
        return cachedNeighbors.getOrPut(this) {
            getNeighborsAndSelf() - this
        }
    }

    fun runSimulationWithSixTimes(cubes: Set<Cube>): Int {
        var activeCubes = cubes
        repeat(6) {
            val nextActiveCubes = mutableSetOf<Cube>()
            val inactiveCubes = activeCubes.map { it.getNeighbors() }.flatten().toSet() - activeCubes
            activeCubes.forEach { activeCube ->
                if (activeCube.getNeighbors().count { it in activeCubes } in 2..3) {
                    nextActiveCubes += activeCube
                }
            }
            inactiveCubes.forEach { inactiveCube ->
                if (inactiveCube.getNeighbors().count { it in activeCubes } == 3) {
                    nextActiveCubes += inactiveCube
                }
            }
            activeCubes = nextActiveCubes
        }
        return activeCubes.size
    }

    fun part1(input: List<String>): Int {
        return runSimulationWithSixTimes(parseCubes(input, 3))
    }

    fun part2(input: List<String>): Int {
        return runSimulationWithSixTimes(parseCubes(input, 4))
    }

    check(part1(readInput("$FOLDER/test")) == 112)
    check(part2(readInput("$FOLDER/test")) == 848)

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