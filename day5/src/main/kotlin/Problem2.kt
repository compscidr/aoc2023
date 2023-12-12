package org.example

import java.io.BufferedReader
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

fun main() {
    Problem2.solve()
}

data class Seed(val start: Long, val range: Long) {}

// target 34039469
object Problem2 {
    fun obtainSeeds(lineReader: BufferedReader): List<Seed> {
        var line = lineReader.readLine() ?: throw RuntimeException("Unexpected end to file")
        val seeds = obtainSeeds(line)
        line = lineReader.readLine()
        assert(line == "") // expect an empty line after seeds
        return seeds
    }

    fun obtainSeeds(line: String): List<Seed> {
        val seedList = mutableListOf<Seed>()
        var position = 7 // line should always start with 'seeds: ' so we can just skip it

        var isFirst = true
        var lastNumber = 0L
        // each number is separated by a space
        while (position < line.length) {
            val spaceLocation = line.indexOf(' ', position)
            val numberString = if (spaceLocation != -1) {
                line.substring(position, spaceLocation)
            } else {
                line.substring(position)
            }
            print("${numberString.toLong()} ")

            if (isFirst) {
                lastNumber = numberString.toLong()
                isFirst = false
            } else {
                val seed = Seed(lastNumber, numberString.toLong())
                seedList.add(seed)
                isFirst = true
            }
            position += numberString.length + 1
        }
        println()
        return seedList.sortedBy { it.start }
    }

    fun parseMapLine(line: String): MapEntry2 {
        var position = 0
        val destinationString = line.substring(position, line.indexOf(' ', position))
        position += destinationString.length + 1
        val sourceString = line.substring(position, line.indexOf(' ', position))
        position += sourceString.length + 1
        val rangeString = line.substring(position)

        val range = rangeString.toLong()
        val start = sourceString.toLong()
        val destinationStart = destinationString.toLong()
        return MapEntry2(start, start + range, destinationStart, destinationStart + range)
    }

    fun obtainMap(mapName: String, lineReader: BufferedReader): Map<Long, MapEntry2> {
        var map = sortedMapOf<Long, MapEntry2>()
        var line = lineReader.readLine() ?: throw RuntimeException("Unexpected end to file")
        assert(line == "$mapName map:") // next line should have the type of map
        line = lineReader.readLine()
        while (line != "") {
            val entry = parseMapLine(line)
            map[entry.sourceStart] = entry
            line = lineReader.readLine() ?: break // handle the case where we reach EOF
        }
        return map
    }

    /**
     * Given a source, determine the destination given the map entries
     */
    fun determineDestinationFromSource(source: Long, map: Map<Long, MapEntry2>): Long {
        for (kv in map) {
            val destination = kv.value.getDestination(source)
            if (destination != -1L) {
                return destination
            }
        }

        // Any source numbers that aren't mapped correspond to the same destination number.
        // So, seed number 10 corresponds to soil number 10.
        return source
    }

    fun solve(inputFile: String = "/day5input.txt") {
        val lineReader = {}.javaClass.getResourceAsStream(inputFile)?.bufferedReader() ?: throw RuntimeException("Error opening file")
        try {
            val seeds = obtainSeeds(lineReader)
            println("Seeds: $seeds")

            val seedToSoilMap = obtainMap("seed-to-soil", lineReader)
            println("seed-to-soil: $seedToSoilMap")
            val soilToFertMap = obtainMap("soil-to-fertilizer", lineReader)
            println("soil-to-fertilizer: $soilToFertMap")
            val fertToWater = obtainMap("fertilizer-to-water", lineReader)
            println("fertilizer-to-water: $fertToWater")
            val waterToLight = obtainMap("water-to-light", lineReader)
            println("water-to-light: $waterToLight")
            val lightToTemp = obtainMap("light-to-temperature", lineReader)
            println("light-to-temperature: $lightToTemp")
            val tempToHumidity = obtainMap("temperature-to-humidity", lineReader)
            println("temperature-to-humidity: $tempToHumidity")
            val humidityToLocation = obtainMap("humidity-to-location", lineReader)
            println("humidity-to-location: $humidityToLocation")

            var minLocation = Long.MAX_VALUE
            // this should give us a 10x speedup since we're trying each seed range in its own thread
            val worker = Executors.newFixedThreadPool(seeds.size)
            val futures = mutableSetOf<Future<Unit>>()
            for (seed in seeds) {
                futures.add(worker.submit(
                    Callable {
                        for (s in seed.start..<seed.start + seed.range) {
                            //println("Trying seed: $s")
                            val soil = determineDestinationFromSource(s, seedToSoilMap)
                            //println("  Soil: $soil")
                            val fert = determineDestinationFromSource(soil, soilToFertMap)
                            //println("  Fert: $fert")
                            val water = determineDestinationFromSource(fert, fertToWater)
                            //println("  Water: $water")
                            val light = determineDestinationFromSource(water, waterToLight)
                            //println("  Light: $light")
                            val temp = determineDestinationFromSource(light, lightToTemp)
                            //println("  Temp: $temp")
                            val humid = determineDestinationFromSource(temp, tempToHumidity)
                            //println("  Humid: $humid")
                            val location = determineDestinationFromSource(humid, humidityToLocation)
                            //println("  Location: $location")
                            if (location < minLocation) {
                                minLocation = location
                            }
                        }
                    }
                ))
            }

            for (f in futures) {
                f.get()
            }
            worker.shutdown()
            worker.awaitTermination(1, TimeUnit.SECONDS)

            println("MinLocation: $minLocation")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}