package ru.mit.spbau.antonpp.parser.compiler

import org.scalatest.{FlatSpec, Matchers}
import ru.mit.spbau.antonpp.parser.parser._

/**
  * @author antonpp
  * @since 24/03/2017
  */
class QueryCompilerSpec extends FlatSpec with Matchers {

    val word1 = "scala"
    val word2 = "java"
    val word3 = "c++"
    val num1 = 2017
    val num2 = 3018

    it should "parse one word query" in {
        QueryCompiler(s"$word1") shouldBe Right(InclusiveMatchNode(word1))
    }

    it should "parse range" in {
        QueryCompiler(s"$$$num1..$$$num2") shouldBe Right(RangeNode(num1, num2, "$"))
    }

    it should "parse number" in {
        QueryCompiler(s"$num1") shouldBe Right(NumberNode(num1))
    }

    it should "parse implicit and" in {
        QueryCompiler(s"$word1 $word2") shouldBe Right(AndNode(InclusiveMatchNode(word1), InclusiveMatchNode(word2)))
    }

    it should "parse explicit or" in {
        QueryCompiler(s"$word1 | $word2") shouldBe Right(OrNode(InclusiveMatchNode(word1), InclusiveMatchNode(word2)))
    }

    it should "parse astersik only on quote" in {
        QueryCompiler("* \"*\"") shouldBe Right(AndNode(InclusiveMatchNode("*"), QuoteNode(List(AsteriskTerm))))
    }

    it should "parse complex query" in {
        // encountered some problems with string interpolation and "-" character
        // thus changed back to simple literals
        QueryCompiler("~scala -java OR 2017..3018 \" -c++ *\" ") shouldBe Right(AndNode(SynonymMatchNode(word1),
            OrNode(ExclusiveMatchNode(word2), AndNode(RangeNode(num1, num2, ""), QuoteNode(List(WordTerm("-" + word3), AsteriskTerm))))))
    }

    it should "not parse malformed AND operator" in {
        QueryCompiler(s"$word1 AND") should be('left)
    }

    it should "not parse malformed OR operator" in {
        QueryCompiler(s"$word1 OR OR $word2") should be('left)
    }
}
