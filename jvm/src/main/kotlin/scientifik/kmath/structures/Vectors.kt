package scientifik.kmath.structures

import scientifik.kmath.operations.*
import java.nio.*


private class RealVectorField(dimension: Int): VectorField<Double>(dimension, DoubleField) {

	override fun produce(initializer: (Int) -> Double): Vector<Double> {
		val buffer = DoubleBuffer.allocate(dimension)
		for(it in 0 until dimension) {
			buffer.put(initializer(it))
		}
		return RealVector(dimension, buffer)
	}
}

private class RealVector(override val dimension: Int, val dataBuffer: DoubleBuffer): Vector<Double> {

	override val context: VectorField<Double> by lazy { RealVectorField(dimension) }

	override val self: Vector<Double> = this

	override fun get(index: Int): Double = dataBuffer[index]

}

actual fun realVector(dimensions: Int, initializer: (Int) -> Double): Vector<Double>
		= RealVectorField(dimensions).produce(initializer)