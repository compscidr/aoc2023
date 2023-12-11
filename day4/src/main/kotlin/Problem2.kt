package org.example

fun main() {
    Problem2.solve()
}

object Problem2 {
    var currentLine = 0
    var totalCards = 0
    val copies = mutableMapOf<Int, Int>()

    fun processLine(line: String, si: Int = 9, ourNumbers: Int = 10, drawnNumbers: Int = 25) {
        totalCards++ // increment for every real card we have
        currentLine++
        if (copies.containsKey(currentLine)) {
            copies[currentLine] = copies[currentLine]!! + 1
        } else {
            copies[currentLine] = 1
        }
        val hashSet = mutableSetOf<Int>()
        var matches = 0
        println("Processing line: $line")
        print("  Our numbers: ")

        // our numbers always start at position 9
        // each number is at most 2 digits + a space
        // we always have ten numbers
        var startIndex = si
        var count = 0
        while (count < ourNumbers) {
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
        while (count < drawnNumbers) {
            val number = if (count == drawnNumbers - 1) {
                line.substring(startIndex, startIndex + 2).trim().toInt() // last number doesn't have a trailing space
            } else {
                line.substring(startIndex, startIndex + 3).trim().toInt()
            }
            print("$number ")
            if (hashSet.contains(number)) {
                matches++
            }
            count++
            startIndex += 3
        }

        // update the copies map
        val multiplier = copies[currentLine]!!
        for (i in 1..matches) {
            if (copies.containsKey(currentLine + i)) {
                copies[currentLine + i] = copies[currentLine + i]!! + multiplier
            } else {
                copies[currentLine + i] = multiplier
            }
        }
    }

    fun solve(inputFile: String = "/day4input.txt", startIndex: Int = 9, ourNumbers: Int = 10, drawnNumbers: Int = 25) {
        val lineReader = {}.javaClass.getResourceAsStream(inputFile)?.bufferedReader()
        try {
            var sum = 0
            var line = lineReader?.readLine()
            while (line != null) {
                processLine(line,startIndex, ourNumbers, drawnNumbers)
                line = lineReader?.readLine()
            }

            for (kv in copies) {
                sum += kv.value
            }

            println("Result: $sum")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}