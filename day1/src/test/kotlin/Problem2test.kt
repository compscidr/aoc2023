package org.example

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Problem2test {
    @Test fun testGetWordValueBackwards() {
        val input = "pdrss6oneone4fournine"
        val expectedOutput = 69

        val output = Problem2.processLine(input)
        assertEquals(expectedOutput, output)
    }

    @Test fun testGetWordValue() {
        val input = "two8five6zfrtjj"
        val expectedOutput = 26
        val output = Problem2.processLine(input)
        assertEquals(expectedOutput, output)
    }
}