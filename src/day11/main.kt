package day11

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day11"

fun main() {

    data class Position(val x: Int, val y: Int)

    data class Graph<T>(val vMap: List<List<T>>, val width: Int = vMap[0].size, val height: Int = vMap.size) {
        fun getPositionIterator(): Iterator<Position> = object : Iterator<Position> {
            var x = 0
            var y = 0
            override fun hasNext() = y < height
            override fun next(): Position {
                val position = Position(x, y)
                x++
                if (x == width) {
                    x = 0
                    y++
                }
                return position
            }
        }

        operator fun get(position: Position): T {
            return vMap[position.y][position.x]
        }
    }

    fun toGraph(charMap: List<List<Char>>): Graph<Char> {
        return Graph(charMap, charMap[0].size, charMap.size)
    }

    fun <T> Position.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Position? {
        val x = this.x + dx
        val y = this.y + dy
        return graph.vMap.getOrNull(y)?.getOrNull(x)?.let { Position(x, y) }
    }

    fun part1(input: List<String>): Int {
        var seats = toGraph(input.map { it.toList() })

        val cachedNeighbors = mutableMapOf<Position, List<Position>>()
        fun Position.getNeighbors(): List<Position> {
            return cachedNeighbors.getOrPut(this) {
                listOfNotNull(
                    move(seats, -1, -1),
                    move(seats, -1, 0),
                    move(seats, -1, 1),
                    move(seats, 0, -1),
                    move(seats, 0, 1),
                    move(seats, 1, -1),
                    move(seats, 1, 0),
                    move(seats, 1, 1),
                )
            }
        }

        while (true) {
            val nextSeatRaw = seats.vMap.map { it.toMutableList() }
            var changed = false
            seats.getPositionIterator().forEach { position ->
                when (seats[position]) {
                    '#' -> if (position.getNeighbors().count { seats[it] == '#' } >= 4) {
                        nextSeatRaw[position.y][position.x] = 'L'
                        changed = true
                    }

                    'L' -> if (position.getNeighbors().none { seats[it] == '#' }) {
                        nextSeatRaw[position.y][position.x] = '#'
                        changed = true
                    }
                }
            }
            seats = toGraph(nextSeatRaw)
            if (!changed) {
                break
            }
        }
        return seats.getPositionIterator().asSequence().count { seats[it] == '#' }
    }

    fun part2(input: List<String>): Int {
        var seats = toGraph(input.map { it.toList() })

        fun Position.getFirstEncounteredSeatAlongDirection(dx: Int, dy: Int): Position? {
            var currentPosition = move(seats, dx, dy) ?: return null
            while (true) {
                val v = seats[currentPosition]
                if (v == '#' || v == 'L') return currentPosition
                currentPosition = currentPosition.move(seats, dx, dy) ?: return null
            }
        }

        val cachedNeighbors = mutableMapOf<Position, List<Position>>()
        fun Position.getAllEncounteredSeatsAlongDirections(): List<Position> {
            return cachedNeighbors.getOrPut(this) {
                listOfNotNull(
                    getFirstEncounteredSeatAlongDirection(-1, -1),
                    getFirstEncounteredSeatAlongDirection(-1, 0),
                    getFirstEncounteredSeatAlongDirection(-1, 1),
                    getFirstEncounteredSeatAlongDirection(0, -1),
                    getFirstEncounteredSeatAlongDirection(0, 1),
                    getFirstEncounteredSeatAlongDirection(1, -1),
                    getFirstEncounteredSeatAlongDirection(1, 0),
                    getFirstEncounteredSeatAlongDirection(1, 1),
                )
            }
        }

        while (true) {
            val nextSeatRaw = seats.vMap.map { it.toMutableList() }
            var changed = false
            seats.getPositionIterator().forEach { position ->
                when (seats[position]) {
                    '#' -> if (position.getAllEncounteredSeatsAlongDirections().count { seats[it] == '#' } >= 5) {
                        nextSeatRaw[position.y][position.x] = 'L'
                        changed = true
                    }

                    'L' -> if (position.getAllEncounteredSeatsAlongDirections().none { seats[it] == '#' }) {
                        nextSeatRaw[position.y][position.x] = '#'
                        changed = true
                    }
                }
            }
            seats = toGraph(nextSeatRaw)
            if (!changed) {
                break
            }
        }
        return seats.getPositionIterator().asSequence().count { seats[it] == '#' }
    }

    check(part1(readInput("$FOLDER/test")) == 37)
    check(part2(readInput("$FOLDER/test")) == 26)

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