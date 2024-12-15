package day20

import println
import readInput
import kotlin.math.sqrt
import kotlin.system.measureNanoTime

private const val FOLDER = "day20"

fun main() {

    data class Jigsaw(val id: Long, private val raw: List<String>, val rotation: Int, val flipped: Boolean) {

        val pattern: List<String> by lazy {
            when (rotation) {
                1 -> raw.first().indices.reversed().map { col -> raw.map { it[col] }.joinToString("") }
                2 -> raw.reversed().map { it.reversed() }
                3 -> raw.first().indices.map { col -> raw.map { it[col] }.joinToString("").reversed() }
                else -> raw
            }.let {
                if (flipped) it.map { line -> line.reversed() }
                else it
            }
        }

        fun edge(idx: Int): String = when (idx) {
            1 -> pattern.map { it.last() }.joinToString("")
            2 -> pattern.last().reversed()
            3 -> pattern.map { it.first() }.joinToString("").reversed()
            else -> pattern.first()
        }
    }

    fun parseJigsaws(input: List<String>): List<Jigsaw> {
        val chunkSize = input.indexOfFirst { it.isBlank() }
        return input.chunked(chunkSize + 1).map { lines ->
            val id = Regex("\\d+").find(lines[0])!!.value.toLong()
            Jigsaw(id, lines.subList(1, chunkSize), 0, false)
        }
    }

    data class Xy(val x: Int, val y: Int) {
        operator fun plus(xy: Xy): Xy = Xy(x + xy.x, y + xy.y)
    }

    data class Image(val content: List<MutableList<Jigsaw?>>) {
        operator fun get(xy: Xy): Jigsaw? {
            return content.getOrNull(xy.y)?.getOrNull(xy.x)
        }

        operator fun set(xy: Xy, jigsaw: Jigsaw?) {
            content[xy.y][xy.x] = jigsaw
        }
    }

    fun solveJigsaws(jigsawsLeft: List<Jigsaw>, image: Image) {
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
                    val rotatedJigsaw = jigsaw.copy(rotation = rotate, flipped = flip)
                    if (
                        neighborOffsetXys.all { xyOffset ->
                            val neighbor = image[xy + xyOffset]!!
                            return@all when {
                                xyOffset.x == -1 -> neighbor.edge(1) == rotatedJigsaw.edge(3).reversed()
                                xyOffset.y == -1 -> neighbor.edge(2) == rotatedJigsaw.edge(0).reversed()
                                else -> throw IllegalArgumentException("Not gonna happen")
                            }
                        }
                    ) {
                        image[xy] = rotatedJigsaw
                        solveJigsaws(jigsawsLeft.toMutableList().apply { remove(jigsaw) }, image)
                        if (image.content.all { it.all { row -> row != null } }) {
                            return
                        }
                        image[xy] = null
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Long {

        val jigsaws = parseJigsaws(input)
        val len = sqrt(jigsaws.size.toDouble()).toInt()
        val image = Image(List(len) { MutableList(len) { null } })

        solveJigsaws(jigsaws, image)

        return image[Xy(0, 0)]!!.id * image[Xy(len - 1, 0)]!!.id * image[Xy(0, len - 1)]!!.id * image[Xy(len - 1, len - 1)]!!.id
    }

    fun part2(input: List<String>): Long {

        val jigsaws = parseJigsaws(input)
        val len = sqrt(jigsaws.size.toDouble()).toInt()
        val image = Image(List(len) { MutableList(len) { null } })

        solveJigsaws(jigsaws, image)

        val actualImage = image.content.joinToString("\n") { jigsawsInRow ->
            val jigsawsActualImage = jigsawsInRow.map { jigsaw ->
                jigsaw!!.pattern.drop(1).dropLast(1).map { it.drop(1).dropLast(1) }
            }
            jigsawsActualImage.first().indices.joinToString("\n") { block -> jigsawsActualImage.joinToString("") { it[block] } }
        }.split("\n").map { it.toMutableList() }

        val seaMonsterPattern = """
                  # 
#    ##    ##    ###
 #  #  #  #  #  #   
        """.trimIndent().split("\n")

        val seaMonster = Jigsaw(0, seaMonsterPattern, 0, true)

        (0..3).forEach { rotate ->
            listOf(false, true).forEach { flip ->
                val currentSeaMonster = seaMonster.copy(rotation = rotate, flipped = flip)
                val pattern = currentSeaMonster.pattern
                (0..actualImage.size - pattern.size).forEach { y ->
                    (0..actualImage.first().size - pattern.first().length).forEach { x ->
                        val matched = pattern.indices.all { dy ->
                            pattern.first().indices.all all2@{ dx ->
                                if (pattern[dy][dx] != '#') return@all2 true
                                actualImage[y + dy][x + dx] != '.'
                            }
                        }
                        if (matched) {
                            pattern.indices.forEach { dy ->
                                pattern.first().indices.forEach forEach2@{ dx ->
                                    if (pattern[dy][dx] != '#') return@forEach2
                                    actualImage[y + dy][x + dx] = 'O'
                                }
                            }
                        }
                    }
                }
            }
        }

        actualImage.joinToString("\n") { it.joinToString("") }.println()

        return actualImage.flatten().count { it == '#' }.toLong()
    }

    check(part1(readInput("$FOLDER/test")) == 20899048083289L)
    check(part2(readInput("$FOLDER/test")) == 273L)

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