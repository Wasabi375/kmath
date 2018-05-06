package scientifik.kmath.structures

import scientifik.kmath.operations.*


class DimensionMismatchException(val expected: Int, val actual: Int) : RuntimeException()

abstract class VectorField<T>(val dimension: Int, val elementField: Field<T>): Field<Vector<T>>{

	private fun checkDim(vararg vectors: Vector<T>){
		for(v in vectors){
			if(v.dimension != dimension){
				throw DimensionMismatchException(dimension, v.dimension)
			}
		}
	}

	/**
	 * creates a new Vector instance
	 */
	abstract fun produce(initializer: (Int) -> T): Vector<T>

	override val one: Vector<T> by lazy { produce {elementField.one} }
	override val zero: Vector<T> by lazy { produce {elementField.zero} }

	override fun divide(a: Vector<T>, b: Vector<T>): Vector<T> {
		checkDim(a, b)
		return produce { elementField.divide(a[it], b[it]) }
	}

	override fun multiply(a: Vector<T>, b: Vector<T>): Vector<T> {
		checkDim(a, b)
		return produce { elementField.multiply(a[it], b[it]) }
	}

	override fun multiply(a: Vector<T>, k: Double): Vector<T> {
		checkDim(a)
		return produce { elementField.multiply(a[it], k) }
	}

	fun multiply(a: Vector<T>, k: T): Vector<T> {
		checkDim(a)
		return produce { elementField.multiply(a[it], k) }
	}

	override fun add(a: Vector<T>, b: Vector<T>): Vector<T> {
		checkDim(a, b)
		return produce { elementField.add(a[it], b[it]) }
	}

	fun scalarProduct(a: Vector<T>, b: Vector<T>): T = sum(a *  b)

	fun sum(a: Vector<T>): T = a.reduce{ acc, it -> elementField.add(acc, it) }
}

interface Vector<T>: FieldElement<Vector<T>>, Iterable<T> {

    override val context: VectorField<T>

	val dimension: Int

	operator fun get(index: Int): T

	override operator fun plus(b: Vector<T>): Vector<T> = context.add(this, b)
	override operator fun minus(b: Vector<T>): Vector<T> = context.add(this, -b)

	override operator fun times(b: Vector<T>): Vector<T> = context.multiply(this, b)
	operator fun times(k: T) = context.multiply(this, k)
    override operator fun times(k: Number) = context.multiply(this, k.toDouble())


	operator fun unaryMinus(): Vector<T> = context.multiply(this, -1.0)

	/**
	 * calculates and returns the scalar product
	 */
	fun dot(b: Vector<T>): T  = context.scalarProduct(this, b)

	override fun iterator(): Iterator<T> = (0 until dimension).asSequence().map { get(it) }.iterator()

	fun sum(): T = context.sum(this)
}

expect fun realVector(dimensions: Int, initializer: (Int) -> Double = {0.0}): Vector<Double>