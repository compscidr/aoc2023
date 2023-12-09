package org.example

fun main() {
    Problem2.solve()
}

object Problem2 {
    // a better way would be to store this a in trie so that we can quickly determine a non-match
    val numberDictionary = mapOf(
        "zero" to 0, "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
    )

    fun isDigit(c: Char): Boolean {
        if (c >= '0' && c <= '9') {
            return true
        } else {
            return false
        }
    }

    /**
     * Makes a number out of the first and second number occuring in a string and returns it.
     */
    fun processLine(line: String): Int {
        println("Processing line: $line")
        val result = getFirstDigit(line) * 10 + getLastDigit(line)
        println("  result: $result")
        return result
    }

    fun getFirstDigit(line: String): Int {
        for (start in 0..line.length-1) {
            println("Starting at: ${line[start]}")
            if (isDigit(line[start])) {
                return line[start] - '0'
            } else {
                val wordVal = getWordValue(line, start)
                if (wordVal != -1) {
                    return wordVal
                }
            }
        }
        return 0 // if we don't find a digit
    }

    /**
     * Given the starting point, see if it matches a number spelled out. If not, return -1
     *
     * Assumes the words are lowercase.
     *
     * Todo: since this fails when we hit a digit, we could make a more complex return type that indicates it failed but
     * then returns the index of the next digit.
     */
    fun getWordValue(line: String, start: Int): Int {
        for (end in start + 1..line.length - 1) {
            val substr = line.substring(start, end)
            println("looking up: $substr")
            if (numberDictionary.containsKey(substr)) {
                return numberDictionary[substr]!!
            }
            if (isDigit(line[end])) {
                return -1
            }
        }
        return -1
    }

    fun getWordValueBackwards(line: String, end: Int): Int {
        for (start in end - 1 downTo 0) {
            if (isDigit(line[end])) {
                return -1
            }
            val substr = line.substring(start, end + 1)
            //println("looking up: $substr")
            if (numberDictionary.containsKey(substr)) {
                return numberDictionary[substr]!!
            }
        }
        return -1
    }

    fun getLastDigit(line: String): Int {
        for (i in line.length - 1 downTo 0) {
            //println("Starting from: ${line[i]}")
            if (isDigit(line[i])) {
                return line[i] - '0'
            } else {
                val wordVal = getWordValueBackwards(line, i)
                if (wordVal != -1) {
                    return wordVal
                }
            }
        }
        return 0 // if we don't find a digit
    }

    fun solve() {
        val lineReader = {}.javaClass.getResourceAsStream("/p1input.txt")?.bufferedReader()
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