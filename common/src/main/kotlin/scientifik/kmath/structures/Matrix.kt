package scientifik.kmath.structures

import scientifik.kmath.operations.*

interface Matrix<T>{

    val rows: Int
    val columns: Int

    operator fun get(i: Int, j: Int): T

    operator fun plus(b: Matrix<T>): Matrix<T>
    operator fun minus(b: Matrix<T>): Matrix<T>
    operator fun times(b: Matrix<T>): Matrix<T>

    operator fun times(b: T): Matrix<T>
    operator fun times(k: Double): Matrix<T>
    operator fun times(k: Number): Matrix<T> = times(k.toDouble())

	fun transpose(): Matrix<T>
	val square: Boolean
		get() = rows == columns
}

expect fun realMatrix(rows: Int, columns: Int,
                      initializer: (Int, Int) -> Double = {_, _-> 0.0}): Matrix<Double>

fun realIdentityMatrix(size: Int): Matrix<Double>
        = realMatrix(size, size) { row, col -> if(row == col) 1.0 else 0.0}