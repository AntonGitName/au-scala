package ru.mit.spbau.antonpp.parser

import ru.mit.spbau.antonpp.parser.compiler.QueryCompiler
import ru.mit.spbau.antonpp.parser.lexer.QueryLexer

/**
  * @author antonpp
  * @since 23/03/2017
  */
object Application {

    def main(args: Array[String]): Unit = {
        val str = "APPLE PEACH OR ~LEMON"
        println(QueryLexer(str))
        println(QueryCompiler(str))
    }

}
