package day19

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

        fun checkMatch(message: String, rule: Rule, depth: Int): String? {
            if (rule.node != null) return if (message.first() == rule.node) message.drop(1) else null
            return rule.branches.map { ruleIds ->
                var messageLeft = message
                for (ruleId in ruleIds) {
                    val branchRule = ruleMap[ruleId] ?: throw IllegalArgumentException("???")
                    messageLeft = checkMatch(messageLeft, branchRule, depth + 1) ?: return@map null
                }
                return@map messageLeft
            }.filterNotNull().minByOrNull { it.length }
        }

        return messages.count { checkMatch(it, ruleMap[0]!!, 0)?.length == 0 }
    }

    fun part2(input: List<String>): Int {
        val (rules, messages) = parseRulesAndMessages(input)

        val ruleMap = rules.associateBy { it.id }.toMutableMap()
        ruleMap[8] = Rule(8, listOf(listOf(42), listOf(42, 8)), null)
        ruleMap[11] = Rule(11, listOf(listOf(42, 31), listOf(42, 11, 31)), null)

        fun checkMatchSizes(message: String, rule: Rule): List<Int> {
            if (message.isEmpty()) return emptyList()
            if (rule.node != null) return if (message.first() == rule.node) listOf(1) else emptyList()
            return rule.branches.map { branchRuleIds ->
                var matchedSizes = listOf(0)
                val branchRulesLeft = branchRuleIds.map { ruleMap[it]!! }.toMutableList()
                while (branchRulesLeft.isNotEmpty()) {
                    val branchRule = branchRulesLeft.removeFirst()
                    val ruleMatchedSizesList = matchedSizes.map { matchedSize ->
                        checkMatchSizes(message.drop(matchedSize), branchRule)
                    }
                    matchedSizes = matchedSizes.zip(ruleMatchedSizesList).map { (s, sList) -> sList.map { it + s } }.flatten()
                }
                matchedSizes
            }.flatten().distinct()
        }

        return messages.count { checkMatchSizes(it, ruleMap[0]!!).maxOrNull() == it.length }
    }

    check(part1(readInput("$FOLDER/test")) == 2)
    check(part2(readInput("$FOLDER/test2")) == 12)

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