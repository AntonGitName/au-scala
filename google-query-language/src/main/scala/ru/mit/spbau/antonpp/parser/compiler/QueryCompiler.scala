package ru.mit.spbau.antonpp.parser.compiler

import ru.mit.spbau.antonpp.parser.lexer.QueryLexer
import ru.mit.spbau.antonpp.parser.parser.{ASTNode, QueryParser}

/**
  * @author antonpp
  * @since 23/03/2017
  */
object QueryCompiler {
    def apply(query: String): Either[QueryCompilerError, ASTNode] = {
        for {
            tokens <- QueryLexer(query).right
            ast <- QueryParser(tokens).right
        } yield ast
    }

}
