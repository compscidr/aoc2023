import org.example.Problem1
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestSampleInput {
    @Test fun testSample() {
        val result = Problem1.solve("/p3test.txt")
        assertEquals(4361, result)
    }
}