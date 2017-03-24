package ru.mit.spbau.antonpp.quaternion

/**
  * @author antonpp
  * @since 07/03/2017
  */
case class Quaternion(a: Double, b: Double, c: Double, d: Double) {

    def this(t: Double) = this(t, 0, 0, 0)

    def normalized: Quaternion = if (abs != 0) {
        this / abs
    } else {
        0.0
    }

    def /(other: Double) = Quaternion(this.a / other, this.b / other, this.c / other, this.d / other)

    def abs: Double = Math.sqrt(absSqr)

    def absSqr: Double = a * a + b * b + c * c + d * d

    def vector: Quaternion = Quaternion(0, b, c, d)

    def dot(other: Quaternion) = Quaternion(this.a * other.a, this.b * other.b, this.c * other.c, this.d * other.d)

    def unary_- = Quaternion(-a, -b, -c, -d)

    def unary_+ = Quaternion(+a, +b, +c, +d)

    def /(other: Quaternion) = this * other.inverse

    def inverse: Quaternion = conjugate / absSqr

    def arg = Math.acos(a / abs)

    def scalarProduct(other: Quaternion): Double = ((this.euclidProduct(other) + other.euclidProduct(this)) * 0.5).a

    def +(other: Quaternion) = Quaternion(this.a + other.a, this.b + other.b, this.c + other.c, this.d + other.d)

    def outerProduct(other: Quaternion) = (this.euclidProduct(other) - other.euclidProduct(this)) * 0.5

    def euclidProduct(other: Quaternion) = this.conjugate * other

    def conjugate: Quaternion = Quaternion(a, -b, -c, -d)

    def vectorProduct(other: Quaternion) = (this * other - other * this) * 0.5

    def *(other: Quaternion): Quaternion = {
        val w = this.a * other.a - this.b * other.b - this.c * other.c - this.d * other.d
        val s = this.a * other.b + this.b * other.a + this.c * other.d - this.d * other.c
        val t = this.a * other.c - this.b * other.d + this.c * other.a + this.d * other.b
        val u = this.a * other.d + this.b * other.c - this.c * other.b + this.d * other.a

        Quaternion(w, s, t, u)
    }

    def -(other: Quaternion) = Quaternion(this.a - other.a, this.b - other.b, this.c - other.c, this.d - other.d)

    def *(other: Double) = Quaternion(this.a * other, this.b * other, this.c * other, this.d * other)
}

object Quaternion {
    val ONE = Quaternion(1, 0, 0, 0)
    val i = Quaternion(0, 1, 0, 0)
    val j = Quaternion(0, 0, 1, 0)
    val k = Quaternion(0, 0, 0, 1)

    implicit def double2Quaternion(d: Double): Quaternion = new Quaternion(d)

    def ln(q: Quaternion): Quaternion = Math.log(q.abs) + q.arg * q.vector.normalized

    def exp(q: Quaternion): Quaternion = {
        (q.vector.normalized * Math.sin(q.vector.abs) + Math.cos(q.vector.abs)) * Math.exp(q.a)
    }

    def sinh(q: Quaternion) = (exp(q) - exp(-q)) * 0.5

    def cosh(q: Quaternion) = (exp(q) + exp(-q)) * 0.5

    def sin(q: Quaternion) = Math.sin(q.a) * cosh(q.vector.abs) + Math.cos(q.a) * sinh(q.vector.abs) * q.vector.normalized

    def cos(q: Quaternion) = Math.cos(q.a) * cosh(q.vector.abs) - Math.sin(q.a) * sinh(q.vector.abs) * q.vector.normalized

    def tan(q: Quaternion) = sin(q) / cos(q)

    def sqr(q: Quaternion) = q * q
}
