package ru.mit.spbau.antonpp.dsl

import java.time.LocalDate

/**
  * @author antonpp
  * @since 25/02/2017
  */
object ImplicitDateBuilder {

    implicit class Day(day: Int) {
        def --(month: Int) = DayMonth(day, month)
    }

    case class DayMonth(day: Int, month: Int) {
        def --(year: Int) = LocalDate.of(year, month, day)
    }

}
