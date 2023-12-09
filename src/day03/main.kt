package day03

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day03"

class TwoDimenGraph<T>(graph: Collection<T>, private val stride: Int) {

    inner class Position(val y: Int, val x: Int) {

        val info: MutableMap<String, Any> by lazy { mutableMapOf() }

        fun up(offset: Int = 1) = if (y - offset >= 0) Position(y - offset, x) else null
        fun down(offset: Int = 1) = if (y + offset < h) Position(y + offset, x) else null
        fun left(offset: Int = 1) = if (x - offset >= 0) Position(y, x - offset) else null
        fun right(offset: Int = 1) = if (x + offset < w) Position(y, x + offset) else null

        fun neighbors() = listOfNotNull(up(), down(), left(), right())

        override fun toString() = "P($y, $x)"

        fun positionString() = "$y,$x"
    }

    fun createPosition(y: Int, x: Int) = Position(y, x)

    private val graph = graph.toMutableList()
    val w = stride
    val h = graph.size / stride

    operator fun get(position: Position): T {
        return graph[position.y * stride + position.x]
    }

    operator fun set(position: Position, value: T): Boolean {
        graph[position.y * stride + position.x] = value
        return true
    }

    override fun toString(): String {
        return graph.chunked(stride).joinToString("\n") { it.joinToString("") }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val graph = TwoDimenGraph(input.flatMap { it.toList() }, input[0].length)

        var position = graph.createPosition(0, 0)

        var treeCount = 0
        while (position.y < graph.h - 1) {
            position = position.down()!!
            position = position.right(3) ?: position.right(3 - graph.w)!!
            treeCount += if (graph[position] == '#') 1 else 0
        }

        return treeCount
    }

    fun part2(input: List<String>): Int {
        val graph = TwoDimenGraph(input.flatMap { it.toList() }, input[0].length)

        val slopes = listOf(
            Pair(1, 1),
            Pair(1, 3),
            Pair(1, 5),
            Pair(1, 7),
            Pair(2, 1)
        )

        val treeCounts = slopes.map { slope ->
            var treeCount = 0
            var position = graph.createPosition(0, 0)
            while (position.y < graph.h - 1) {
                position = position.down(slope.first)!!
                position = position.right(slope.second) ?: position.right(slope.second - graph.w)!!
                treeCount += if (graph[position] == '#') 1 else 0
            }
            treeCount
        }

        return treeCounts.reduce { acc, i -> acc * i }
    }

    check(part1(readInput("$FOLDER/test")) == 7)
    check(part2(readInput("$FOLDER/test")) == 336)

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