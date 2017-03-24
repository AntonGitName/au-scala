package ru.mit.spbau.antonpp.quaternion

import org.scalactic.Equality
import org.scalatest.{FunSuite, Matchers}
import ru.mit.spbau.antonpp.quaternion.Quaternion._

/**
  * @author antonpp
  * @since 24/03/2017
  */
class QuaternionTest extends FunSuite with Matchers {

    test("constants") {
        i should equal(Quaternion(0, 1, 0, 0))
        j should equal(Quaternion(0, 0, 1, 0))
        k should equal(Quaternion(0, 0, 0, 1))
    }

    test("conjugate") {
        Quaternion(1, 3, 5, 7).conjugate shouldBe Quaternion(1, -3, -5, -7)
    }

    test("addition") {
        val res = Quaternion(1, 2, 3, 4) + Quaternion(0, 1, 2, 3)
        res shouldBe Quaternion(1, 3, 5, 7)
    }

    test("substraction") {
        val res = Quaternion(1, 2, 2, 1) - Quaternion(0, 1, 2, 3)
        res shouldBe Quaternion(1, 1, 0, -2)
    }

    // all further checks done by wolfram
    // https://www.wolframalpha.com/examples/Quaternions.html

    test("multiplication") {
        val res = Quaternion(1, 2, 2, 1) * Quaternion(0, 1, 2, 3)
        res shouldBe Quaternion(-9, 5, -3, 5)
    }

    val eps = 1e-4

    implicit val quaternionEq = new Equality[Quaternion] {
        override def areEqual(a: Quaternion, b: Any): Boolean = {
            b match {
                case q: Quaternion => a.a === q.a +- eps && a.b === q.b +- eps && a.c === q.c +- eps && a.d === q.d +- eps
                case _ => false
            }
        }
    }

    test("division") {
        val res = Quaternion(10, 7, 4, 1) / Quaternion(-5, 12, 2.3, 9)
        //        0.204473 - 0.73916 i + 0.0313366 j - 0.24717 k
        res should ===(Quaternion(0.204473, -0.73916, 0.0313366, -0.24717))
    }

    test("inverse") {
        //        0.060241 - 0.0421687i - 0.0240964j - 0.0060241k
        Quaternion(10, 7, 4, 1).inverse should ===(0.060241 - 0.0421687 * i - 0.0240964 * j - 0.0060241 * k)
    }

    // cannot reliably test operations on quaternions as I do not have any tool to calculate exp(Quaternion)
    // but we can test that operations are correct over complex field at least and that will not be a proof
    // that the implementation is correct but a big part of it.

    test("exp") {
        exp(Quaternion(4, 3.9, 0, 0)) should ===(-39.6346 - 37.5508 * i)
    }

    test("log") {
        ln(Quaternion(4, 3.9, 0, 0)) should ===(1.72037 + 0.772741 * i)
    }

    test("sinh") {
        sinh(Quaternion(4, 3.9, 0, 0)) should ===(-19.8106 - 18.7817 * i)
    }

    test("cosh") {
        cosh(Quaternion(4, 3.9, 0, 0)) should ===(-19.8239 - 18.7691 * i)
    }

    test("sin") {
        val q = Quaternion(4, 3.9, 0, 0)
        sin(Quaternion(4, 3.9, 0, 0)) should ===(-18.7016 - 16.1392 * i)
    }

    test("cos") {
        val q = Quaternion(4, 3.9, 0, 0)
        cos(Quaternion(4, 3.9, 0, 0)) should ===(-16.1524 + 18.6863 * i)
    }

    test("cos^2 + sin^2 = 1") {
        val q = Quaternion(3.1415, 2.71828, 1.618, 1.414)
        sqr(cos(q)) + sqr(sin(q)) should ===(ONE)
    }

    test("cosh^2 - sinh^2 = 1") {
        val q = Quaternion(3.1415, 2.71828, 1.618, 1.414)
        sqr(cosh(q)) - sqr(sinh(q)) should ===(ONE)
    }

}
