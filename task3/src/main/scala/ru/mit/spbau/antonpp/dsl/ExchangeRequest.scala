package ru.mit.spbau.antonpp.dsl

import java.time.LocalDate


/**
  * @author antonpp
  * @since 03/03/2017
  */
sealed abstract class ExchangeRequest(amount: Double, from: Currency, to: Currency) {
    def represent: String = s"from $from to $to"

    override def toString: String = ExchangeRequest.ExchangeRequestToString(this)
}

object ExchangeRequest {

    implicit def ExchangeRequestToString(exchangeRequest: ExchangeRequest): String = {
        try {
            val rate = exchangeRequest match {
                case exchangeRequest: LatestExchangeRequest => exchangeRequest.amount * CurrencyApiCaller.getExchangeRate(exchangeRequest.from, exchangeRequest.to)
                case exchangeRequest: HistoryExchangeRequest => exchangeRequest.amount * CurrencyApiCaller.getExchangeRate(exchangeRequest.from, exchangeRequest.to, exchangeRequest.date)
            }
            rate.toString
        } catch {
            case e: Exception => s"En error occurred while requesting specified exchange rates (${exchangeRequest.represent})"
        }
    }

    implicit class ExchangeBuilder(amount: Double) {

        object usd extends ExchangeInput(amount, Currency.usd)

        object rub extends ExchangeInput(amount, Currency.rub)

        object eur extends ExchangeInput(amount, Currency.eur)

        object jpy extends ExchangeInput(amount, Currency.jpy)

        object gbp extends ExchangeInput(amount, Currency.gbp)

    }

    case class HistoryExchangeRequest(amount: Double, from: Currency, to: Currency, date: LocalDate) extends ExchangeRequest(amount, from, to)

    case class LatestExchangeRequest(amount: Double, from: Currency, to: Currency) extends ExchangeRequest(amount, from, to) {
        def on(date: LocalDate) = HistoryExchangeRequest(amount, from, to, date)
    }

    case class ExchangeInput(amount: Double, from: Currency) {
        def to(to: Currency): LatestExchangeRequest = LatestExchangeRequest(amount, from, to)
    }

}
