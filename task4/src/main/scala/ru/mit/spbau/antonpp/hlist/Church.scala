package ru.mit.spbau.antonpp.hlist

/**
  * @author Anton Mordberg
  * @since 03.03.17
  */
sealed trait Church

object Church {

    type ChurchNull = _0.type
    final val _1 = Succ(_0)
    final val _2 = Succ(_1)
    final val _3 = Succ(_2)
    final val _4 = Succ(_3)
    final val _5 = Succ(_4)
    final val _6 = Succ(_5)
    final val _7 = Succ(_6)
    final val _8 = Succ(_7)
    final val _9 = Succ(_8)

    case class Succ[T <: Church](prev: T) extends Church

    //    sealed class ChurchNull extends Church
    final case object _0 extends Church

}