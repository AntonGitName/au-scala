package ru.mit.spbau.antonpp.hlist

import ru.mit.spbau.antonpp.hlist.Church.{ChurchNull, Succ}
import ru.mit.spbau.antonpp.hlist.HList.{HCons, HNil}

/**
  * @author Anton Mordberg
  * @since 04.03.17
  */
trait HListSplitter[ListT <: HList, Where <: Church, L <: HList, R <: HList] {
    def apply(list: ListT, where: Where): (L, R)
}

object HListSplitter {

    implicit def defaultSplit[ListT <: HList]: HListSplitter[ListT, ChurchNull, HNil, ListT] =
        (list: ListT, pos: ChurchNull) => (HNil, list)

    implicit def recursiveSplit[H, A <: HList, Pos <: Church, L <: HList, R <: HList](implicit splitter: HListSplitter[A, Pos, L, R])
    : HListSplitter[HCons[H, A], Succ[Pos], HCons[H, L], R] =
        (list: HCons[H, A], pos: Succ[Pos]) => {
            val (l, r) = splitter(list.tail, pos.prev)
            (HCons(list.head, l), r)
        }
}