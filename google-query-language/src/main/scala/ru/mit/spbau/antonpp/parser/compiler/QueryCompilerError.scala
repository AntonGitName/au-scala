package ru.mit.spbau.antonpp.parser.compiler

/**
  * @author antonpp
  * @since 23/03/2017
  */
trait QueryCompilerError

case class QueryLexerError(location: Location, msg: String) extends QueryCompilerError

case class QueryParserError(location: Location, msg: String) extends QueryCompilerError

case class Location(line: Int, column: Int) {
    override def toString = s"$line:$column"
}
