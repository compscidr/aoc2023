package org.example

import java.io.BufferedReader
import java.io.FileReader

fun main() {
    Problem1.solve()
}

object Problem1 {

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
        for (c in line) {
            if (c >= '0' && c <= '9') {
                return c - '0'
            }
        }
        return 0 // if we don't find a digit
    }

    fun getLastDigit(line: String): Int {
        for (i in line.length - 1 downTo 0) {
            if (line[i] >= '0' && line[i] <= '9') {
                return line[i] - '0'
            }
        }
        return 0
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