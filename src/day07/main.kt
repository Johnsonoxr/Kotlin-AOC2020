package day07

import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day07"

fun main() {

    data class BagCount(val bag: String, val count: Int)

    fun parseRules(input: List<String>): Map<String, List<BagCount>> {
        return input.associate { line ->
            val (bag, contents) = line.replace(Regex(" bags| bag|\\."), "").split(" contain ")
            if ("no other" == contents) return@associate bag to emptyList()
            val bagCounts = contents.split(", ").map { content ->
                content.split(" ", limit = 2).let { BagCount(it[1], it[0].toInt()) }
            }
            bag to bagCounts
        }
    }

    fun part1(input: List<String>): Int {
        val rules = parseRules(input)

        fun hasShinyGold(bag: String): Boolean {
            val bagCounts = rules[bag]!!
            return when {
                bagCounts.any { it.bag == "shiny gold" } -> true
                else -> bagCounts.any { hasShinyGold(it.bag) }
            }
        }

        return rules.keys.count { hasShinyGold(it) }
    }

    fun part2(input: List<String>): Int {
        val rules = parseRules(input)

        fun calcBagCount(bag: String): Int {
            val bagCounts = rules[bag]!!
            return bagCounts.sumOf { it.count * (1 + calcBagCount(it.bag)) }
        }

        return calcBagCount("shiny gold")
    }

    check(part1(readInput("$FOLDER/test")) == 4)

    val input = readInput("$FOLDER/input")
    val part1Result: Int
    val part1Time = measureNanoTime {
        part1Result = part1(input)
    }

    check(part2(readInput("$FOLDER/test")) == 32)

    val part2Result: Int
    val part2Time = measureNanoTime {
        part2Result = part2(input)
    }

    println("Part 1 result: $part1Result")
    println("Part 2 result: $part2Result")
    println("Part 1 takes ${part1Time / 1e6f} milliseconds.")
    println("Part 2 takes ${part2Time / 1e6f} milliseconds.")
}