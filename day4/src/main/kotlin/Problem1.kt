package org.example

fun main() {
    Problem1.solve()
}

object Problem1 {
    fun processLine(line: String): Int {
        val hashSet = mutableSetOf<Int>()
        var score = 0
        println("Processing line: $line")
        print("  Our numbers: ")

        // our numbers always start at position 9
        // each number is at most 2 digits + a space
        // we always have ten numbers
        var startIndex = 9
        var count = 0
        while (count < 10) {
            val number = line.substring(startIndex, startIndex + 3).trim().toInt()
            hashSet.add(number)
            print("$number ")
            count++
            startIndex += 3
        }

        // account for separator " | "
        startIndex += 3

        // there are always 25 numbers drawn, each takes at most 2 digits + a space
        print(" Drawn numbers: ")
        count = 0
        while (count < 25) {
            val number = if (count == 24) {
                line.substring(startIndex, startIndex + 2).trim().toInt() // last number doesn't have a trailing space
            } else {
                line.substring(startIndex, startIndex + 3).trim().toInt()
            }
            print("$number ")
            if (hashSet.contains(number)) {
                if (score == 0) {
                    score = 1
                } else {
                    score *= 2
                }
            }
            count++
            startIndex += 3
        }

        println()
        println("Score: $score")

        return score
    }

    fun solve() {
        val lineReader = {}.javaClass.getResourceAsStream("/day4input.txt")?.bufferedReader()
        try {
            var sum = 0
            var line = lineReader?.readLine()
            while (line != null) {
                sum += processLine(line)
                line = lineReader?.readLine()
            }
            println("Result: $sum")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}