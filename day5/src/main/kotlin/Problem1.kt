package org.example

import java.io.BufferedReader

fun main() {
    Problem1.solve()
}

data class MapEntry(val destination: Long, val source: Long, val range: Long) {
    /**
     * Returns the destination if the map entry supports the source, otherwise
     * returns -1
     */
    fun getDestination(candidate: Long): Long {
        if (candidate < source) {
            return -1
        }
        if (candidate > source + range) {
            return -1
        }
        val diff = candidate - source
        return destination + diff
    }
}

object Problem1 {
    fun obtainSeeds(lineReader: BufferedReader): List<Long> {
        var line = lineReader.readLine() ?: throw RuntimeException("Unexpected end to file")
        val seeds = obtainSeeds(line)
        line = lineReader.readLine()
        assert(line == "") // expect an empty line after seeds
        return seeds
    }

    fun obtainSeeds(line: String): List<Long> {
        val seedList = mutableListOf<Long>()
        var position = 7 // line should always start with 'seeds: ' so we can just skip it

        // each number is separated by a space
        while (position < line.length) {
            val spaceLocation = line.indexOf(' ', position)
            val numberString = if (spaceLocation != -1) {
                line.substring(position, spaceLocation)
            } else {
                line.substring(position)
            }
            print("${numberString.toLong()} ")
            seedList.add(numberString.toLong())
            position += numberString.length + 1
        }
        println()
        return seedList
    }

    fun parseMapLine(line: String): MapEntry {
        var position = 0
        val destinationString = line.substring(position, line.indexOf(' ', position))
        position += destinationString.length + 1
        val sourceString = line.substring(position, line.indexOf(' ', position))
        position += sourceString.length + 1
        val rangeString = line.substring(position)

        return MapEntry(destinationString.toLong(), sourceString.toLong(), rangeString.toLong())
    }

    fun obtainMap(mapName: String, lineReader: BufferedReader): List<MapEntry> {
        val list = mutableListOf<MapEntry>()
        var line = lineReader.readLine() ?: throw RuntimeException("Unexpected end to file")
        assert(line == "$mapName map:") // next line should have the type of map
        line = lineReader.readLine()
        while (line != "") {
            val entry = parseMapLine(line)
            list.add(entry)
            line = lineReader.readLine() ?: break // handle the case where we reach EOF
        }
        return list
    }

    /**
     * Given a source, determine the destination given the map entries
     */
    fun determineDestinationFromSource(source: Long, map: List<MapEntry>): Long {
        for (entry in map) {
            val destination = entry.getDestination(source)
            if (destination != -1L) {
                return destination
            }
        }

        // Any source numbers that aren't mapped correspond to the same destination number.
        // So, seed number 10 corresponds to soil number 10.
        return source
    }

    fun solve(inputFile: String = "/day5input.txt"): Long {
        val lineReader = {}.javaClass.getResourceAsStream(inputFile)?.bufferedReader() ?: throw RuntimeException("Error opening file")
        try {
            val seeds = obtainSeeds(lineReader)
            println("Seeds: $seeds")
            val seedToSoilMap = obtainMap("seed-to-soil", lineReader)
            println(seedToSoilMap)
            val soilToFertMap = obtainMap("soil-to-fertilizer", lineReader)
            println(soilToFertMap)
            val fertToWater = obtainMap("fertilizer-to-water", lineReader)
            println(fertToWater)
            val waterToLight = obtainMap("water-to-light", lineReader)
            println(waterToLight)
            val lightToTemp = obtainMap("light-to-temperature", lineReader)
            println(lightToTemp)
            val tempToHumidity = obtainMap("temperature-to-humidity", lineReader)
            println(tempToHumidity)
            val humidityToLocation = obtainMap("humidity-to-location", lineReader)
            println(humidityToLocation)

            // start min location with the max location
            var minLocation = Long.MAX_VALUE

            for (seed in seeds) {
                val soil = determineDestinationFromSource(seed, seedToSoilMap)
                val fert = determineDestinationFromSource(soil, soilToFertMap)
                val water = determineDestinationFromSource(fert, fertToWater)
                val light = determineDestinationFromSource(water, waterToLight)
                val temp = determineDestinationFromSource(light, lightToTemp)
                val humid = determineDestinationFromSource(temp, tempToHumidity)
                val location = determineDestinationFromSource(humid, humidityToLocation)
                if (location < minLocation) {
                    minLocation = location
                }
            }
            println("Min location: $minLocation")
            return minLocation
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}