package day19

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day19"

fun main() {

    data class Rule(val id: Int, val branches: List<List<Int>>, val node: Char?)

    fun parseRulesAndMessages(input: List<String>): Pair<List<Rule>, List<String>> {
        val rules = input.take(input.indexOf("")).map { line ->
            val (id, description) = line.split(": ")
            when {
                description.startsWith('"') -> Rule(id.toInt(), emptyList(), description.trim('"').first())
                else -> Rule(id.toInt(), description.split(" | ").map { it.split(" ").map { n -> n.toInt() } }, null)
            }
        }
        val messages = input.drop(input.indexOf("") + 1)
        return rules to messages
    }

    fun part1(input: List<String>): Int {
        val (rules, messages) = parseRulesAndMessages(input)

        val ruleMap = rules.associateBy { it.id }

        fun checkMatch(message: String, rule: Rule): String {
            if (rule.node != null) return if (message.first() == rule.node) message.drop(1) else message
            val unmatched = rule.branches.mapNotNull { ruleIds ->
                var messageLeft = message
                for (ruleId in ruleIds) {
                    val branchRule = ruleMap[ruleId] ?: throw IllegalArgumentException("???")
                    messageLeft = checkMatch(messageLeft, branchRule).takeIf { it != messageLeft } ?: return@mapNotNull null
                }
                return@mapNotNull messageLeft
            }
            return unmatched.minByOrNull { it.length } ?: message
        }

        return messages.filter { checkMatch(it, rules[0]).isEmpty() }.also { it.forEach { it.println() } }.count()
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    check(part1(readInput("$FOLDER/test")) == 2)
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