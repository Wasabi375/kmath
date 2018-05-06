package scientifik.kmath.structures

import org.junit.Assert.assertEquals
import kotlin.test.*

class RealMatrixTest {

	val matrix1 = realMatrix(3, 3) { r, c -> (r + c).toDouble()}
	val matrix2 = realMatrix(3, 4) { r, c -> (r - c).toDouble()}
	val matrix3 = realMatrix(4, 3) { r, c -> (r * c).toDouble()}
	val identity = realIdentityMatrix(3)

	@Test
	fun testMultiplication() {

		val r1 = matrix1 * identity
		assertEquals(r1, matrix1)

		val r2 = matrix3 * identity
		assertEquals(r2, matrix3)

		val r3 = matrix1 * matrix2
		/*
		(5 | 2 | -1 | -4
		 8 | 2 | -4 | -10
		 11 | 2 | -7 | -16)
		 */
		assertEquals(5.0, r3[0, 0], 0.1)
		assertEquals(-16.0, r3[2, 3], 0.1)


		val r4 = matrix2 * matrix3
		/*
		(0 | -14 | -28
		 0 | -8 | -16
		 0 | -2 | -4)
		 */
		assertEquals(0.0, r4[1, 0], 0.1)
		assertEquals(-14.0, r4[0, 1], 0.1)

		assertFails {
			matrix2 * matrix1
		}
	}

	@Test
	fun testGeneration() {
		for(r in 0..2){
			for(c in 0..2){
				assertEquals("error at [$r, $c]", (r + c).toDouble(), matrix1[r, c], 0.1)
			}
		}
	}
}