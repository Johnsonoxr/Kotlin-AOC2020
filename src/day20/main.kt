package day20

import println
import readInput
import kotlin.math.sqrt
import kotlin.system.measureNanoTime

private const val FOLDER = "day20"

fun main() {

    fun part1(input: List<String>): Long {

        data class Jigsaw(
            val id: Long, val pattern: List<String>, val edgePatterns: List<String> = listOf(
                pattern.first(),
                pattern.map { it.last() }.joinToString(""),
                pattern.last().reversed(),
                pattern.map { it.first() }.joinToString("").reversed()
            )
        ) {
//            val edgePatterns = listOf(
//                pattern.first(),
//                pattern.map { it.last() }.joinToString(""),
//                pattern.last().reversed(),
//                pattern.map { it.first() }.joinToString("").reversed()
//            )
        }

        fun parseJigsaws(input: List<String>): List<Jigsaw> {
            val chunkSize = input.indexOfFirst { it.isBlank() }
            return input.chunked(chunkSize + 1).map { lines ->
                val id = Regex("\\d+").find(lines[0])!!.value.toLong()
                Jigsaw(id, lines.subList(1, chunkSize))
            }
        }

        data class Xy(val x: Int, val y: Int) {
            operator fun plus(xy: Xy): Xy = Xy(x + xy.x, y + xy.y)
        }

        val jigsaws = parseJigsaws(input)
        val len = sqrt(jigsaws.size.toDouble()).toInt()

        data class Image(val content: List<MutableList<Jigsaw?>>) {
            operator fun get(xy: Xy): Jigsaw? {
                return content.getOrNull(xy.y)?.getOrNull(xy.x)
            }

            operator fun set(xy: Xy, jigsaw: Jigsaw?) {
                content[xy.y][xy.x] = jigsaw
            }
        }

        val image = Image(List(len) { MutableList(len) { null } })

        fun solveJigsaws(jigsawsLeft: List<Jigsaw>) {
            val y = image.content.indexOfFirst { it.any { row -> row == null } }.takeIf { it >= 0 } ?: return
            val x = image.content[y].indexOfFirst { it == null }
            val xy = Xy(x, y)
            val neighborOffsetXys = listOfNotNull(
                Xy(-1, 0).takeIf { image[xy + it] != null },
                Xy(0, -1).takeIf { image[xy + it] != null },
            )
            jigsawsLeft.forEach { jigsaw ->
                (0..3).forEach { rotate ->
                    listOf(false, true).forEach { flip ->
                        val edgePatterns = if (flip) jigsaw.edgePatterns.reversed().map { it.reversed() } else jigsaw.edgePatterns
                        val rotatedJigsaw = jigsaw.copy(edgePatterns = edgePatterns.takeLast(rotate) + edgePatterns.take(4 - rotate))
                        if (
                            neighborOffsetXys.all { xyOffset ->
                                val neighbor = image[xy + xyOffset]!!
                                return@all when {
                                    xyOffset.x == -1 -> neighbor.edgePatterns[1] == rotatedJigsaw.edgePatterns[3].reversed()
                                    xyOffset.y == -1 -> neighbor.edgePatterns[2] == rotatedJigsaw.edgePatterns[0].reversed()
                                    else -> throw IllegalArgumentException("Not gonna happen")
                                }
                            }
                        ) {
                            image[xy] = rotatedJigsaw
                            solveJigsaws(jigsawsLeft.toMutableList().apply { remove(jigsaw) })
                            if (image.content.all { it.all { row -> row != null } }) {
                                return
                            }
                            image[xy] = null
                        }
                    }
                }
            }
        }

        solveJigsaws(jigsaws)

        return image[Xy(0, 0)]!!.id * image[Xy(len - 1, 0)]!!.id * image[Xy(0, len - 1)]!!.id * image[Xy(len - 1, len - 1)]!!.id
    }

    fun part2(input: List<String>): Long {
        return 1
    }

    check(part1(readInput("$FOLDER/test")) == 20899048083289L)
    check(part2(readInput("$FOLDER/test")) == 1L)

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