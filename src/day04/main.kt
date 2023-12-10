package day04

import println
import readInput
import kotlin.system.measureNanoTime

private const val FOLDER = "day04"

fun main() {

    fun part1(input: List<String>): Int {
        val passports = mutableListOf<String>()
        passports.add("")
        input.forEach { line ->
            if (line.isBlank()) {
                passports.add("")
            } else {
                passports[passports.lastIndex] += "$line "
            }
        }

        val requiredFields = listOf(
            "byr:",
            "iyr:",
            "eyr:",
            "hgt:",
            "hcl:",
            "ecl:",
            "pid:"
        )

        return passports.count { passport -> requiredFields.all { it in passport } }
    }

    fun part2(input: List<String>): Int {
        val passports = mutableListOf<String>()
        passports.add("")
        input.forEach { line ->
            if (line.isBlank()) {
                passports.add("")
            } else {
                passports[passports.lastIndex] += "$line "
            }
        }

        val sorting = listOf(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid",
            "cid"
        )

        for ((index, s) in passports.withIndex()) {
            passports[index] = s.split(" ").filter { it.isNotEmpty() }.sortedBy { sorting.indexOf(it.substring(0, 3)) }.joinToString(" ") + " "
        }

        val byrFailed = mutableListOf<String>()
        val iyrFailed = mutableListOf<String>()
        val eyrFailed = mutableListOf<String>()
        val hgtFailed = mutableListOf<String>()
        val hclFailed = mutableListOf<String>()
        val eclFailed = mutableListOf<String>()
        val pidFailed = mutableListOf<String>()

        val (validPassport, invalidPassport) = passports.partition { passport ->
            val isByrValid = "byr:(\\d{4})".toRegex().find(passport)?.groupValues?.last()?.let { it.toInt() in 1920..2002 } ?: false
            if (!isByrValid) {
                byrFailed.add(passport)
                return@partition false
            }

            val isIyrValid = "iyr:(\\d{4})".toRegex().find(passport)?.groupValues?.last()?.let { it.toInt() in 2010..2020 } ?: false
            if (!isIyrValid) {
                iyrFailed.add(passport)
                return@partition false
            }

            val isEyrValid = "eyr:(\\d{4})".toRegex().find(passport)?.groupValues?.last()?.let { it.toInt() in 2020..2030 } ?: false
            if (!isEyrValid) {
                eyrFailed.add(passport)
                return@partition false
            }

            val isHgtValid = "hgt:(\\d+)(cm|in)".toRegex().find(passport)?.let { match ->
                val (height, unit) = match.destructured
                when (unit) {
                    "cm" -> height.toInt() in 150..193
                    "in" -> height.toInt() in 59..76
                    else -> false
                }
            } ?: false
            if (!isHgtValid) {
                hgtFailed.add(passport)
                return@partition false
            }

            val isHclValid = "hcl:#[0-9a-f]{6}".toRegex().find(passport) != null
            if (!isHclValid) {
                hclFailed.add(passport)
                return@partition false
            }

            val isEclValid = "ecl:(amb|blu|brn|gry|grn|hzl|oth)".toRegex().find(passport) != null
            if (!isEclValid) {
                eclFailed.add(passport)
                return@partition false
            }

            val isPidValid = "pid:[0-9]{9} ".toRegex().find(passport) != null
            if (!isPidValid) {
                pidFailed.add(passport)
                return@partition false
            }

            return@partition true
        }

        validPassport.forEach {
            it.println()
        }

        "=========================".println()

        "\nByr failed: ${byrFailed.size}".println()
        byrFailed.forEach {
            it.println()
        }

        "\nIyr failed: ${iyrFailed.size}".println()
        iyrFailed.forEach {
            it.println()
        }

        "\nEyr failed: ${eyrFailed.size}".println()
        eyrFailed.forEach {
            it.println()
        }

        "\nHgt failed: ${hgtFailed.size}".println()
        hgtFailed.forEach {
            it.println()
        }

        "\nHcl failed: ${hclFailed.size}".println()
        hclFailed.forEach {
            it.println()
        }

        "\nEcl failed: ${eclFailed.size}".println()
        eclFailed.forEach {
            it.println()
        }

        "\nPid failed: ${pidFailed.size}".println()
        pidFailed.forEach {
            it.println()
        }

        return validPassport.size
    }

    check(part1(readInput("$FOLDER/test")) == 2)

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