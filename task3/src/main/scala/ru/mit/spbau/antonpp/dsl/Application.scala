package ru.mit.spbau.antonpp.dsl

import ru.mit.spbau.antonpp.dsl.Currency._
import ru.mit.spbau.antonpp.dsl.ExchangeRequest._
import ru.mit.spbau.antonpp.dsl.ImplicitDateBuilder._

/**
  * @author antonpp
  * @since 25/02/2017
  */
object Application {
    def main(args: Array[String]): Unit = {
        println(1000.usd to rub)
        println(1000.usd to rub on 1--1--2010)
        println(1000.usd to rub on 1--1--1000)
        println(1000.jpy to eur)
        println(1.rub to gbp on 1--1--2005)
        println(1.rub to gbp on 1--1--2014)
    }
}
