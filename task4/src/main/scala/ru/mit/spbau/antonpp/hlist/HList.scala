package ru.mit.spbau.antonpp.hlist

import ru.mit.spbau.antonpp.hlist.HList.HCons

/**
  * @author Anton Mordberg
  * @since 03.03.17
  */
sealed trait HList {
    def ::[T](v: T): HCons[T, this.type] = HCons(v, this)
}

object HList {
    type ::[+H, +T <: HList] = HCons[H, T]
    val :: = HCons

    def splitAt[T <: HList, Pos <: Church, L <: HList, R <: HList](list: T, pos: Pos)
                                                                  (implicit splitter: HListSplitter[T, Pos, L, R])
    : (L, R) = splitter(list, pos)

    sealed class HNil extends HList

    final case class HCons[+H, +T <: HList](head: H, tail: T) extends HList {
        override def toString: String = head.toString + " :: " + tail.toString
    }

    case object HNil extends HNil {
        override def toString: String = "nil"
    }

}

