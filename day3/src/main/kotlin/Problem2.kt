package org.example

fun main() {
    Problem2.solve()
}

object Problem2 {
    var lineCount = 1

    // if we encounter a star, check the map for co-ordinate. if already in map, add multiply current value against it
    // and add it to the sum, otherwise, just store the current value in the map

    // co-ordinate is line, index
    val gearMap = mutableMapOf<Pair<Int,Int>, Int>() // map co-ordinate of star to a previously adjacent value

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
                sum += checkStar(line, lineBefore, lineAfter, startIndex, endIndex, lineCount)
                startIndex = endIndex
            } else {
                startIndex++
            }
        }
        println()

        println("Sum $sum")
        return sum
    }

    fun isStar(c: Char): Boolean {
        return c == '*'
    }

    /**
     * If we are adjacent to a star, check the map and if star position not there, add it and return 0,
     * else, retrieve and multiply by current value and return it
     */
    fun checkStar(line: String, lineBefore: String?, lineAfter: String?, startIndex: Int, endIndex: Int, lineNumber: Int): Int {
        val potentialPartString = line.substring(startIndex, endIndex)
        // before and after
        if (startIndex > 0) {
            if (isStar(line[startIndex-1])) {
                val key = Pair(lineNumber, startIndex-1)
                if (gearMap.containsKey(key)) {
                    return potentialPartString.toInt() * gearMap[key]!!
                } else {
                    gearMap[key] = potentialPartString.toInt()
                }
            }
        }

        if (endIndex < line.length) {
            if (isStar(line[endIndex])) {
                val key = Pair(lineNumber, endIndex)
                if (gearMap.containsKey(key)) {
                    return potentialPartString.toInt() * gearMap[key]!!
                } else {
                    gearMap[key] = potentialPartString.toInt()
                }
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
                if (isStar(lineBefore[start])) {
                    val key = Pair(lineNumber-1, start)
                    if (gearMap.containsKey(key)) {
                        return potentialPartString.toInt() * gearMap[key]!!
                    } else {
                        gearMap[key] = potentialPartString.toInt()
                    }
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
                if (isStar(lineAfter[start])) {
                    val key = Pair(lineNumber+1, start)
                    if (gearMap.containsKey(key)) {
                        return potentialPartString.toInt() * gearMap[key]!!
                    } else {
                        gearMap[key] = potentialPartString.toInt()
                    }
                }
                start++
            }
        }

        return 0
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