package day16

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day16"

fun main() {
    fun part1(input: List<String>): Int {

        data class ValidFields(val range1: IntRange, val range2: IntRange)

        val ranges = input.take(input.indexOf("")).map { line ->
            val numbers = Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
            ValidFields(numbers[0]..numbers[1], numbers[2]..numbers[3])
        }

        val nearbyTickets = input.drop(input.indexOfLast { it == "nearby tickets:" } + 1).map { line ->
            line.split(",").map { it.toInt() }
        }

        val flattenRanges = ranges.map { listOf(it.range1, it.range2) }.flatten()
        return nearbyTickets.flatten().filter { ticketNumber -> flattenRanges.none { ticketNumber in it } }.sum()
    }

    fun part2(input: List<String>): Int {

        data class ValidFields(val name: String, val range1: IntRange, val range2: IntRange) {
            operator fun contains(int: Int): Boolean {
                return int in range1 || int in range2
            }
        }

        val fields = input.take(input.indexOf("")).map { line ->
            val numbers = Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
            ValidFields(line.split(": ")[0], numbers[0]..numbers[1], numbers[2]..numbers[3])
        }

        val myTicket = input[input.indexOf("your ticket:") + 1].split(",").map { it.toInt() }

        val nearbyTickets = input.drop(input.indexOfLast { it == "nearby tickets:" } + 1).map { line ->
            line.split(",").map { it.toInt() }
        }

        val flattenRanges = fields.map { listOf(it.range1, it.range2) }.flatten()

        val validTickets = nearbyTickets.filter { ticket ->
            ticket.all { number -> flattenRanges.any { number in it } }
        }

        val idxFieldMap = mutableMapOf<Int, ValidFields>()

        val validTicketsCandidates = mutableMapOf<Int, List<Int>>() //  idx -> set of numbers
        validTickets[0].indices.forEach { idx ->
            validTicketsCandidates[idx] = validTickets.map { it[idx] }
        }

        val fieldCandidates = fields.toMutableList()
        while (fieldCandidates.isNotEmpty()) {
            var noChange = true
            for (field in fieldCandidates) {
                val matchedMap = validTicketsCandidates.filterValues { numbers -> numbers.all { it in field } }
                if (matchedMap.size == 1) {
                    val idx = matchedMap.keys.first()
                    idxFieldMap[idx] = field
                    fieldCandidates.remove(field)
                    validTicketsCandidates.remove(idx)
                    noChange = false
                    break
                }
            }
            if (noChange) throw IllegalArgumentException("Stocked...")
        }

        val myTicketMap = mutableMapOf<String, Int>()
        myTicket.forEachIndexed { index, number ->
            myTicketMap[idxFieldMap[index]!!.name] = number
        }

        myTicketMap.println()
        myTicketMap.filterKeys { it.startsWith("departure") }.values.takeIf { it.isNotEmpty() }?.let {
            "departures: ${it.map { n -> n.toLong() }.reduce { acc, i -> acc * i }}".println()
        }

        return 1
    }

    check(part1(readInput("$FOLDER/test")) == 71)
    check(part2(readInput("$FOLDER/test2")) == 1)

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