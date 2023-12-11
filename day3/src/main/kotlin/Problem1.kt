package org.example

fun main() {
    Problem1.solve()
}

object Problem1 {
    var lineCount = 1

    fun processLine(line: String, lineBefore: String?, lineAfter: String?): Int {
        println("Processing: line ${lineCount++}")
        println(lineBefore)
        println(line)
        println(lineAfter)
        print("Parts: ")
        var sum = 0

        var startIndex = 0
        while (startIndex < line.length) {
            // identify the next possible part start and end index in the line
            if (line[startIndex] in '0'..'9') {
                var endIndex = startIndex + 1
                while (line[endIndex] in '0'..'9') {
                    endIndex++
                    if (endIndex >= line.length) {
                        break
                    }
                }
                val potentialPartString = line.substring(startIndex, endIndex)
                //print("$potentialPartString ")
                val result = checkPart(line, lineBefore, lineAfter, startIndex, endIndex)
                //print("$result ")
                if (result) {
                    print("$potentialPartString ")
                    sum += potentialPartString.toInt()
                }
                startIndex = endIndex
            } else {
                startIndex++
            }
        }
        println()



        println("Sum $sum")
        return sum
    }

    fun isSymbol(c: Char): Boolean {
        if (c in '1'..'9') {
            return false
        }
        if (c == '.') {
            return false
        }
        return true
    }

    fun checkPart(line: String, lineBefore: String?, lineAfter: String?, startIndex: Int, endIndex: Int): Boolean {
        // before and after
        if (startIndex > 0) {
            if (line[startIndex-1] != '.') {
                return true
            }
        }

        if (endIndex < line.length) {
            if (line[endIndex] != '.') {
                return true
            }
        }

        // above (check up to 1 before and after endIndex) to get diagonals
        if (lineBefore != null) {
            var start = if (startIndex > 0) {
                startIndex - 1
            } else {
                0
            }
            while (start <= endIndex) {
                if (start >= line.length) {
                    break
                }
                if (isSymbol(lineBefore[start])) {
                    return true
                }
                start++
            }
        }

        if (lineAfter != null) {
            var start = if (startIndex > 0) {
                startIndex - 1
            } else {
                0
            }
            while (start <= endIndex) {
                if (start >= line.length) {
                    break
                }
                if (isSymbol(lineAfter[start])) {
                    return true
                }
                start++
            }
        }

        return false
    }

    // since we need to know above and below the current line, we can't process one line at a time anymore, we need
    // the line before and the line after as well
    fun solve(inputFile: String = "/p3input.txt"): Int {
        var previousLine: String? = null
        var currentLine: String? = null
        var nextLine: String? = null
        val lineReader = {}.javaClass.getResourceAsStream(inputFile)?.bufferedReader()
        try {
            var sum = 0
            var line = lineReader?.readLine()
            while (line != null) {
                if (previousLine == null) {
                    previousLine = line
                } else {
                    if (currentLine == null) {
                        currentLine = line
                        sum += processLine(previousLine, null, currentLine) // edge case where we're just starting
                    } else {
                        if (nextLine == null) {
                            nextLine = line
                            sum += processLine(currentLine, previousLine, nextLine)
                        } else {
                            previousLine = currentLine
                            currentLine = nextLine
                            nextLine = line
                            sum += processLine(currentLine, previousLine, nextLine)
                        }
                    }
                }
                line = lineReader?.readLine()
            }

            sum += processLine(nextLine!!, currentLine, null)

            println("Result: $sum")
            return sum
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}