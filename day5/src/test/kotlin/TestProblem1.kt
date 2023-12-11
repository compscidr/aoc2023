import org.example.Problem1
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestProblem1 {
    @Test fun testObtainSeeds() {
        val seeds = Problem1.obtainSeeds("seeds: 79 14 55 13")
        assertEquals(listOf(79L, 14L, 55L, 13L), seeds)
    }

    @Test fun testSoilToFertilizerMap() {
        val lineReader = {}.javaClass.getResourceAsStream("/day5testinput.txt")?.bufferedReader()
            ?: throw RuntimeException("Error opening file")
        val seeds = Problem1.obtainSeeds(lineReader)
        assertEquals(listOf(79L, 14L, 55L, 13L), seeds)

        val soilMap = Problem1.obtainMap("seed-to-soil", lineReader)

        assertEquals(81, Problem1.determineDestinationFromSource(79, soilMap))
        assertEquals(14, Problem1.determineDestinationFromSource(14, soilMap))
        assertEquals(57, Problem1.determineDestinationFromSource(55, soilMap))
        assertEquals(13, Problem1.determineDestinationFromSource(13, soilMap))
    }

    @Test fun intStringTest() {
        val string = "3766866638"
        println(string.toLong())
    }

    @Test fun fullSolution() {
        val result = Problem1.solve("/day5testinput.txt")
        assertEquals(35L, result)
    }
}