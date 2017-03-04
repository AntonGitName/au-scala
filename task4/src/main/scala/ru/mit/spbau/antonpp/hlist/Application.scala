package ru.mit.spbau.antonpp.hlist

import ru.mit.spbau.antonpp.hlist.Church._2
import ru.mit.spbau.antonpp.hlist.HList._

/**
  * @author Anton Mordberg
  * @since 03.03.17
  */
object Application {
    def main(args: Array[String]): Unit = {
        val hlist = 1 :: 2f :: "3" :: List(4) :: HNil

        //        val (l, r) = hlist.splitAt(_2)
        val (l, r) = splitAt(hlist, _2)
        println("LIST: ")
        println(hlist)
        println()
        println("L: ")
        println(l)
        println()
        println("R: ")
        println(r)
    }
}
