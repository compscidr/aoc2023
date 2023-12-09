package org.example

fun main() {
    Problem1.solve()
}

object Problem1 {
    val maxMap = mapOf<String, Int>(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    fun processLine(line: String): Int {
        var invalid = false
        println("Processing line $line")

        // extract game number, assume it starts with "Game " (with a space) and then the number, then a colon.
        val colonIndex = line.indexOf(":", 5) // skip the word "Game "
        val wordString = line.substring(5, colonIndex)
        val gameNumber = wordString.toInt()
        print("  Game number: $gameNumber")

        var position = colonIndex + 2 // skip the colon and the space following it
        while (position < line.length) {
            // extract quantity which always go from a starting value to a space before a color is spelled out
            val endOfNumber = line.indexOf(" ", position)
            val quantity = line.substring(position, endOfNumber).toInt()
            position = endOfNumber + 1

            print(" Quantity: `$quantity`")

            // the color always go to a comma or a semi colon, or end of line
            val nextComma = line.indexOf(",", position)
            val nextSemi = line.indexOf(";", position)
            val color = if (nextComma == -1 && nextSemi == -1) {
                line.substring(position, line.length)
            } else if (nextComma == -1) {
                line.substring(position, nextSemi)
            } else if (nextSemi == -1) {
                line.substring(position, nextComma)
            } else if (nextComma < nextSemi ) {
                line.substring(position, nextComma)
            } else {
                line.substring(position, nextSemi)
            }
            print(" Color: `$color`")
            position += color.length + 2

            if (quantity > maxMap[color]!!) {
                print("INVALID $color $quantity > ${maxMap[color]}")
                invalid = true // could just return 0 here and short circuit but verbose will help debug
            }
        }
        println()

        if (invalid) {
            return 0
        } else {
            return gameNumber
        }
    }

    fun solve() {
        val lineReader = {}.javaClass.getResourceAsStream("/p2input.txt")?.bufferedReader()
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