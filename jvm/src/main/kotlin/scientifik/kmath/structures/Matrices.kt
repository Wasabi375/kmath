package scientifik.kmath.structures

import scientifik.kmath.operations.*
import scientifik.kmath.structures.*

private abstract class MatrixImpl<T> (
		override val rows: Int,
		override val columns: Int,
		val field: Field<T>)
	: Matrix<T> {


	override operator fun plus(b: Matrix<T>): Matrix<T> {
		require(b.rows == rows && b.columns == columns) {
			"dimension of both matricies must be the same"
		}

		return createMatrix(rows, columns) { row, col ->
			field.add(this[row, col], b[row, col])
		}
	}
	override operator fun minus(b: Matrix<T>): Matrix<T> {
		require(b.rows == rows && b.columns == columns) {
			"dimension of both matricies must be the same"
		}

		return createMatrix(rows, columns) { row, col ->
			field.add(this[row, col], field.multiply(b[row, col], -1.0))
		}
	}

	override fun times(b: Matrix<T>): Matrix<T>  {

		require(columns == b.rows) {
			"The columnDimension of the right matrix must be the same as the rowDimension of the left matrix"
		}

		return createMatrix(rows, b.columns) { row, col ->
			(0 until columns).map {
				field.multiply(this[row, it], b[it, col])
			}.reduce{ a, b ->
				field.add(a, b)
			}
		}
	}

	// TODO do we need scalar plus/minus
	override operator fun times(b: T): Matrix<T> = createMatrix(rows, columns) { row, col ->
		field.multiply(this[row, col], b)
	}
	override operator fun times(k: Double): Matrix<T> = createMatrix(rows, columns) { row, col ->
		field.multiply(this[row, col], k)
	}

	override fun transpose(): Matrix<T>
			= createMatrix(columns, rows) { row, col -> this[col, row]}

	abstract fun createMatrix(rows: Int, columns: Int, initializer: (Int, Int) -> T): Matrix<T>
}



private class RealMatrix(rows: Int,
                         columns: Int,
                         initializer: (Int, Int) -> Double = {_, _ -> 0.0 })
	: MatrixImpl<Double>(rows, columns, DoubleField){

	val data = real2DArray(rows, columns) { row, col -> initializer(row, col) }

	override fun get(i: Int, j: Int): Double = data.get(i, j)

	override fun createMatrix(rows: Int, columns: Int, initializer: (Int, Int) -> Double): Matrix<Double>
			= RealMatrix(rows, columns, initializer)

	override fun equals(other: Any?): Boolean {

		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as RealMatrix

		return data == other.data
	}

	override fun hashCode(): Int {
		return 37 * rows +
		       37 * columns +
		       37 * data.hashCode()
	}

}

actual fun realMatrix(rows: Int, columns: Int, initializer: (Int, Int) -> Double): Matrix<Double>
		= RealMatrix(rows, columns, initializer)
