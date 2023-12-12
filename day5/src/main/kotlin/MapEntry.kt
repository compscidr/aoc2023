package org.example

data class MapEntry(val destination: Long, val source: Long, val range: Long) {
    /**
     * Returns the destination if the map entry supports the source, otherwise
     * returns -1
     */
    fun getDestination(target: Long): Long {
        if (target < source) {
            return -1
        }
        if (target >= source + range) {
            return -1
        }

        val diff = target - source
        return destination + diff
    }
}

data class MapEntry2(val sourceStart: Long, val sourceEnd: Long, val destinationStart: Long, val destinationEnd: Long) {
    /**
     * Returns the destination if the map entry supports the source, otherwise
     * returns -1
     */
    fun getDestination(target: Long): Long {
        if (target < sourceStart) {
            return -1
        }
        if (target >= sourceEnd) {
            return -1
        }

        val diff = target - sourceStart
        return destinationStart + diff
    }
}