package ru.mit.spbau.antonpp.parser.lexer

import ru.mit.spbau.antonpp.parser.compiler.{Location, QueryLexerError}

import scala.util.parsing.combinator.RegexParsers

/**
  * @author antonpp
  * @since 23/03/2017
  */
object QueryLexer extends RegexParsers {

    override val whiteSpace = """[ \n\t\r\f]+""".r
    val rangeRegex = """([$€]?)(\d+)\.\.\1(\d+)""".r

    override def skipWhitespace = true

    def word: Parser[Word] = positioned {
        "[^\\s\"]+".r ^^ { str => SimpleWord(str) }
    }

    def minus: Parser[Word] = positioned {
        "-[^\\s\"]+".r ^^ { str => MinusWord(str) }
    }

    def tilde: Parser[Word] = positioned {
        "~[^\\s\"]+".r ^^ { str => TildeWord(str) }
    }

    def number: Parser[Number] = positioned {
        "[0-9]+".r ^^ { str => Number(str.toInt) }
    }

    def and = positioned {
        "AND" ^^ (_ => And)
    }

    def quote = positioned {
        "\"" ^^ (_ => Quote)
    }

    def range = positioned {
        rangeRegex ^^ { case rangeRegex(m, s, e) => Range(s.toInt, e.toInt, m) }
    }

    def asterisk = positioned {
        "*" ^^ (_ => Asterisk)
    }

    def or = positioned {
        "(OR)|\\|".r ^^ (x => Or(x))
    }

    def tokens: Parser[List[QueryToken]] = {
        phrase(rep1(and | or | quote | range | asterisk | tilde | minus | number | word)) ^^ {
            tokens: List[QueryToken] => processQuotes(tokens)
        }
    }

    def apply(query: String): Either[QueryLexerError, List[QueryToken]] = {
        parse(tokens, query) match {
            case NoSuccess(msg, next) => Left(QueryLexerError(Location(next.pos.line, next.pos.column), msg))
            case Success(result, next) => Right(result)
        }
    }

    private def processQuotes(tokens: List[QueryToken], quoteOpened: Boolean = false): List[QueryToken] = {
        tokens.headOption match {
            // close quote
            case Some(Quote) if quoteOpened => Quote :: processQuotes(tokens.tail)
            // open quote
            case Some(Quote) if !quoteOpened => Quote :: processQuotes(tokens.tail, quoteOpened = true)
            // Asterisk operator used only in quotes
            case Some(Asterisk) if quoteOpened => Asterisk :: processQuotes(tokens.tail, quoteOpened)
            case Some(Asterisk) if !quoteOpened => SimpleWord("*") :: processQuotes(tokens.tail, quoteOpened)
            // Everything except Asterisk inside a quote is a word
            case Some(token: Word) if quoteOpened => SimpleWord(token.original) :: processQuotes(tokens.tail, quoteOpened)
            case Some(token: QueryToken) if quoteOpened => SimpleWord(token.representation) :: processQuotes(tokens.tail, quoteOpened)
            case Some(token: QueryToken) if !quoteOpened => token :: processQuotes(tokens.tail, quoteOpened)
            // close last quote
            case None if quoteOpened => List(Asterisk)
            case None => List()
        }
    }

}
