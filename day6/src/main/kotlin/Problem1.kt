package org.example

fun main() {
    Problem1.solve()
}

object Problem1 {

    /**
     * Returns the integer parsed and the position after the integer was parsed or -1,-1 if not found
     */
    fun parseInt(line: String, startIndex: Int): Pair<Int, Int> {
        var startInt = -1
        for (i in startIndex..line.length - 1) {
            if (line[i] in '0'..'9') {
                startInt = i
                break
            }
        }

        if (startInt == -1) {
            return Pair(-1, -1)
        }
        var endInt = -1
        for (i in startInt + 1 .. line.length - 1) {
            if (line[i] !in '0'..'9') {
                endInt = i
                break
            }
        }
        if (endInt == -1) {
            endInt = line.length
        }

        val intString = line.substring(startInt, endInt)
        return Pair(intString.toInt(), endInt)
    }

    fun getNumberOfWins(totalTime: Int, currentRecord: Int): Int {
        var wins = 0
        for (i in 1..totalTime - 1) {
            val distance = getDistance(i, totalTime)
            if (distance > currentRecord) {
                wins++
            }
        }
        return wins
    }

    /**
     * Figure out for an amount of time pressed, and the total time available, how far the boat will go
     *
     * Your toy boat has a starting speed of zero millimeters per millisecond. For each whole millisecond you spend
     * at the beginning of the race holding down the button, the boat's speed increases by one millimeter per
     * millisecond.
     */
    fun getDistance(timePressed: Int, totalTime: Int): Int {
        if (timePressed == 0) {
            return 0
        }
        if (timePressed == totalTime) {
            return 0
        }
        val timeRemaining = totalTime - timePressed
        return timePressed * timeRemaining
    }

    fun solve(inputFile: String = "/day6input.txt") {
        val lineReader = {}.javaClass.getResourceAsStream(inputFile)?.bufferedReader() ?: throw RuntimeException("Error opening file")
        val timeLine = lineReader.readLine()
        val distanceLine = lineReader.readLine()
        lineReader.close()

        val timePosition = timeLine.indexOf("Time:")
        if (timePosition == -1) {
            throw RuntimeException("Expecting the line to start with 'Time:'")
        }

        val times = mutableListOf<Int>()
        var position = timePosition + 5
        while (position < timeLine.length) {
            val parseResult = parseInt(timeLine, position)
            if (parseResult.first == -1) {
                break
            } else {
                times.add(parseResult.first)
            }
            position = parseResult.second
        }

        val distancePosition = distanceLine.indexOf("Distance:")
        if (distancePosition == -1) {
            throw RuntimeException("Expecting the line to start with 'Distance:'")
        }
        val distances = mutableListOf<Int>()
        position = distancePosition + 9
        while (position < distanceLine.length) {
            val parseResult = parseInt(distanceLine, position)
            if (parseResult.first == -1) {
                break
            } else {
                distances.add(parseResult.first)
            }
            position = parseResult.second
        }

        val wins = mutableListOf<Int>()
        for (i in 0..< times.size) {
            val time = times[i]
            val distance = distances[i]
            wins.add(getNumberOfWins(time, distance))
        }

        var result = wins[0]
        for (i in 1 ..< wins.size) {
            result *= wins[i]
        }

        println("Result: $result")
    }
}