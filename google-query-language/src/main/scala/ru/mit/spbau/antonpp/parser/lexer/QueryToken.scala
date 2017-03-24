package ru.mit.spbau.antonpp.parser.lexer

import scala.util.parsing.input.Positional

/**
  * @author antonpp
  * @since 23/03/2017
  */
sealed trait QueryToken extends Positional {
    def representation: String
}

abstract class Word(str: String, prefix: Int) extends QueryToken {
    override def representation: String = str.substring(prefix)

    def original = str
}

case class SimpleWord(str: String) extends Word(str, 0)

case class TildeWord(str: String) extends Word(str, 1)

case class MinusWord(str: String) extends Word(str, 1)

case class Number(value: Int) extends QueryToken {
    override def representation: String = value.toString
}

case object Quote extends QueryToken {
    override def representation: String = "\""
}

case object Asterisk extends QueryToken {
    override def representation: String = "*"
}

case object And extends QueryToken {
    override def representation: String = "AND"
}

case class Or(str: String) extends QueryToken {
    override def representation: String = str

    override def toString: String = "Or"
}

case class Range(start: Int, end: Int, modifier: String) extends QueryToken {
    override def representation: String = s"$modifier$start..$modifier$end"
}
