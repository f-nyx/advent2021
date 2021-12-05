package be.rlab.aoc2021.challenge

import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Vector2(
    val x: Double,
    val y: Double
) {
    companion object {
        fun new(
            x: Double,
            y: Double
        ): Vector2 = Vector2(x, y)
    }

    val length: Double = sqrt(x * x + y * y)
    val normalized: Vector2 by lazy {
        this / length
    }
    val rotation: Double = atan(y / x)

    fun distanceTo(other: Vector2): Double {
        return (other - this).length
    }

    /** Rotates this vector and returns a new vector with the required rotation.
     * @param angleInRad Rotation angle, in radians.
     * @return the rotated vector.
     */
    fun rotate(angleInRad: Double): Vector2 {
        return copy(
            x = x * cos(angleInRad) - y * sin(angleInRad),
            y = x * sin(angleInRad) + y * cos(angleInRad)
        )
    }

    /** Calculates the projection of this vector on another vector.
     * @param vector Vector to project this vector to.
     * @return The projected vector.
     */
    fun projection(vector: Vector2): Vector2 {
        return vector.normalized * (this * vector)
    }

    /** Performs the cross product between this vector and another vector.
     * @param vector Vector to perform the cross product.
     * @return the cross product between the two vectors.
     */
    infix fun x(vector: Vector2): Double {
        return x * vector.y - y * vector.x
    }

    operator fun plus(vector: Vector2): Vector2 {
        return copy(
            x = x + vector.x,
            y = y + vector.y
        )
    }

    operator fun minus(vector: Vector2): Vector2 {
        return copy(
            x = x - vector.x,
            y = y - vector.y
        )
    }

    operator fun times(scalar: Double): Vector2 = copy(
        x = x * scalar,
        y = y * scalar
    )

    operator fun times(vector: Vector2): Double {
        return x * vector.x + y * vector.y
    }

    operator fun div(scalar: Double): Vector2 = copy(
        x = x / scalar,
        y = y / scalar
    )
}
