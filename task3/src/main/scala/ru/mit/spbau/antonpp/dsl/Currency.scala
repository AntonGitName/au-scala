package ru.mit.spbau.antonpp.dsl

/**
  * @author antonpp
  * @since 03/03/2017
  */
sealed case class Currency(value: String)

object Currency {

    val values = Seq(rub, usd, eur)

    object rub extends Currency("RUB")

    object usd extends Currency("USD")

    object eur extends Currency("EUR")

    object jpy extends Currency("JPY")

    object gbp extends Currency("GBP")

}
