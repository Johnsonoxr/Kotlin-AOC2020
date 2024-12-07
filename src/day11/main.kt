package day11

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day11"

fun main() {

    data class Node<T>(val v: T, val x: Int, val y: Int)

    data class Graph<T>(val vMap: List<List<T>>, val width: Int, val height: Int) {
        fun getNodeIterator(): Iterator<Node<T>> = object : Iterator<Node<T>> {
            var x = 0
            var y = 0
            override fun hasNext() = y < height
            override fun next(): Node<T> {
                val node = Node(vMap[y][x], x, y)
                x++
                if (x == width) {
                    x = 0
                    y++
                }
                return node
            }
        }
    }

    fun toGraph(charMap: List<List<Char>>): Graph<Char> {
        return Graph(charMap, charMap[0].size, charMap.size)
    }

    fun <T> Node<T>.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Node<T>? {
        val x = this.x + dx
        val y = this.y + dy
        return graph.vMap.getOrNull(y)?.getOrNull(x)?.let { Node(it, x, y) }
    }

    fun <T> Node<T>.getNeighbors(graph: Graph<T>): List<Node<T>> {
        return listOfNotNull(
            move(graph, -1, -1),
            move(graph, -1, 0),
            move(graph, -1, 1),
            move(graph, 0, -1),
            move(graph, 0, 1),
            move(graph, 1, -1),
            move(graph, 1, 0),
            move(graph, 1, 1),
        )
    }

    fun part1(input: List<String>): Int {
        var seats = toGraph(input.map { it.toList() })
        while (true) {

            val nextSeatRaw = seats.vMap.map { it.toMutableList() }
            var changed = false
            seats.getNodeIterator().forEach { seat ->
                when (seat.v) {
                    '#' -> if (seat.getNeighbors(seats).count { it.v == '#' } >= 4) {
                        nextSeatRaw[seat.y][seat.x] = 'L'
                        changed = true
                    }
                    'L' -> if (seat.getNeighbors(seats).none { it.v == '#' }) {
                        nextSeatRaw[seat.y][seat.x] = '#'
                        changed = true
                    }
                }
            }
            seats = toGraph(nextSeatRaw)
            if (!changed) {
                break
            }
        }
        return seats.getNodeIterator().asSequence().count { it.v == '#' }
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    check(part1(readInput("$FOLDER/test")) == 37)
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