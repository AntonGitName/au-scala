package ru.mit.spbau.antonpp.dsl

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.io.Source

/**
  * @author antonpp
  * @since 25/02/2017
  */
object CurrencyApiCaller {
    val api = "http://api.fixer.io"

    def getExchangeRate(from: Currency, to: Currency): Double = {
        getRateInternal(from, to, "latest")
    }

    def getExchangeRate(from: Currency, to: Currency, date: LocalDate): Double = {
        val str = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        getRateInternal(from, to, str)
    }

    private def getRateInternal(from: Currency, to: Currency, date: String): Double = {
        val base: String = from.value
        val symbol: String = to.value
        val url = s"$api/$date?base=$base&symbols=$symbol"
        val json = Source.fromURL(url).mkString.parseJson
        json.asJsObject.fields("rates").asJsObject.fields(symbol).convertTo[Double]
    }

    //    def main(args: Array[String]): Unit = {
    //        println(getLatestExchangeRate(CurrencyRate.rub, CurrencyRate.eur))
    //        println(getDateExchangeRate(CurrencyRate.usd, CurrencyRate.eur, LocalDate.of(2000, 3, 1)))
    //    }
}
