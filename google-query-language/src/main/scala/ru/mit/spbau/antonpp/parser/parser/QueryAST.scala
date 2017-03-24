package ru.mit.spbau.antonpp.parser.parser

import scala.util.parsing.input.Positional

/**
  * @author antonpp
  * @since 23/03/2017
  */
sealed trait ASTNode extends Positional

sealed trait BinOpNode extends ASTNode

case class AndNode(a: ASTNode, b: ASTNode) extends BinOpNode

case class OrNode(a: ASTNode, b: ASTNode) extends BinOpNode

sealed trait QuoteTerm

case class WordTerm(str: String) extends QuoteTerm

case object AsteriskTerm extends QuoteTerm

case class QuoteNode(terms: List[QuoteTerm]) extends ASTNode

case class SynonymMatchNode(word: String) extends ASTNode

case class ExclusiveMatchNode(word: String) extends ASTNode

case class InclusiveMatchNode(word: String) extends ASTNode

case class RangeNode(start: Int, end: Int, modifier: String) extends ASTNode

case class NumberNode(value: Int) extends ASTNode
