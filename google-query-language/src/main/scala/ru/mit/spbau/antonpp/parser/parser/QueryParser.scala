package ru.mit.spbau.antonpp.parser.parser

import ru.mit.spbau.antonpp.parser.compiler.{Location, QueryParserError}
import ru.mit.spbau.antonpp.parser.lexer._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

/**
  * @author antonpp
  * @since 23/03/2017
  */
object QueryParser extends Parsers {
    override type Elem = QueryToken

    def apply(tokens: Seq[QueryToken]): Either[QueryParserError, ASTNode] = {
        val reader = new QueryTokenReader(tokens)
        longQuery(reader) match {
            case NoSuccess(msg, next) => Left(QueryParserError(Location(next.pos.line, next.pos.column), msg))
            case Success(result, next) => Right(result)
        }
    }

    private def andQuery: Parser[ASTNode] = positioned {
        query ~ (and ~> longQuery) ^^ { case q1 ~ q2 => AndNode(q1, q2) } |
                query ~ longQuery ^^ { case q1 ~ q2 => AndNode(q1, q2) }
    }

    private def orQuery: Parser[ASTNode] = positioned {
        query ~ (or ~> longQuery) ^^ { case q1 ~ q2 => OrNode(q1, q2) }
    }

    private def longQuery: Parser[ASTNode] = positioned {
        phrase(orQuery | andQuery | query)
    }

    private def query: Parser[ASTNode] = positioned {
        sWord | tWord | mWord | quote | range | number
    }

    private def number: Parser[ASTNode] = positioned {
        accept("number", { case x: Number => NumberNode(x.value) })
    }

    private def quote: Parser[ASTNode] = positioned {
        Quote ~> rep1(term) <~ Quote ^^ (terms => QuoteNode(terms.map({
            case Asterisk => AsteriskTerm
            case x => WordTerm(x.representation)
        })))
    }

    private def term: Parser[QueryToken] = positioned {
        accept("term", {
            case x: SimpleWord => x
            case x: Asterisk.type => x
        })
    }

    private def sWord: Parser[ASTNode] = positioned {
        accept("word", { case x: SimpleWord => InclusiveMatchNode(x.representation) })
    }

    private def mWord: Parser[ASTNode] = positioned {
        accept("-word", { case x: MinusWord => ExclusiveMatchNode(x.representation) })
    }

    private def tWord: Parser[ASTNode] = positioned {
        accept("~word", { case x: TildeWord => SynonymMatchNode(x.representation) })
    }

    private def and: Parser[And.type] = positioned {
        accept("AND", { case a@And => a })
    }

    private def or: Parser[Or] = positioned {
        accept("OR", { case o@Or(_) => o })
    }

    private def range: Parser[ASTNode] = positioned {
        accept("range", { case Range(s, e, m) => RangeNode(s, e, m) })
    }

    class QueryTokenReader(tokens: Seq[QueryToken]) extends Reader[QueryToken] {
        override def first: QueryToken = tokens.head

        override def atEnd: Boolean = tokens.isEmpty

        override def pos: Position = tokens.headOption.map(_.pos).getOrElse(NoPosition)

        override def rest: Reader[QueryToken] = new QueryTokenReader(tokens.tail)
    }

}
