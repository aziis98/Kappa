package com.aziis98.kappa.parser

import com.aziis98.kappa.structure.LinearGraph
import java.util.*

// Copyright 2016 Antonio De Lucreziis

object ParserCSS : AbstractParser() {

    override fun gluer(a: Char, b: Char): Boolean {
        return (a.isLetter() && b.isLetter())
                || (a.isDigit() && b.isDigit())
                || (a.isDigit() && b == '.')
                || (a == '.' && b.isDigit())
                || (a == '\\' && b.isLetter())
    }

    override fun refineTokens(tokens: List<String>): List<String> {
        return tokens
    }

    override fun parse(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {
        while (tokens.isNotEmpty()) {
            parseObject(tokens, context)
        }
    }

    fun parseObject(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {

        val name = tokens.pop()

        assert(tokens.pop() == "{")

        while (tokens.peek() != "}") {

        }

        assert(tokens.pop() == "}")

    }

    fun parseKeyValue(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {

    }

    fun readValue(tokens: LinkedList<String>) : String {
        val token = tokens.peek()
        if (token == "\"") {
            return tokens.pop().apply {
                assert(tokens.pop() == "\"")
            }
        }
        else {
            try {
                return token.toDouble().toString()
            } catch (e: NumberFormatException) {
                return tokens.dropWhile { it != ";" || it != "}" }.joinToString("")
            }
        }
    }

}