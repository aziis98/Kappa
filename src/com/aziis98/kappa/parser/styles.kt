package com.aziis98.kappa.parser

import com.aziis98.kappa.structure.LinearGraph
import com.aziis98.utils.merge
import com.aziis98.utils.templateOf
import com.aziis98.utils.templatesOf
import org.jetbrains.annotations.Mutable
import java.util.*

// Copyright 2016 Antonio De Lucreziis

object ParserCSS : AbstractParser() {

    override fun gluer(a: Char, b: Char): Boolean {
        return (a.isLetter() && b.isLetter())
                || (a.isDigit() && b.isDigit())
                || (a.isLetter() && b.isDigit())
                || (a.isDigit() && b == '.')
                || (a == '.' && b.isDigit())
                || (a == '.' && b.isLetter())
                || (a.isLetter() && b == '-')
                || (a == '-' && b.isLetter())
                || (a == '\\' && b.isLetter())
                || (a == '#' && b.isDigit() )
                || (a == '#' && b.isLetter() )
    }

    override fun refineTokens(tokens: List<String>): List<String> {
        return tokens
                .merge({ it.joinToString("") }, templatesOf(
                        templateOf("string", "\"", "\""),
                        templateOf("comment", "/*", "*/")
                ))
                .filter { !it.isBlank() }
    }

    override fun parse(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {
        while (tokens.isNotEmpty()) {
            parseObject(tokens, context)
        }
    }

    fun parseObject(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {

        val name = tokens.popWhile { it != "{" }.joinToString(" ")
        val localContext =  context.put(name, name)

        assert(tokens.pop() == "{")

        while (tokens.peek() != "}") {
            val (key, value) = readKeyValue(tokens)

            // println("$name -> $key : $value")

            localContext.put(key, value)
            assert(tokens.pop() == ";")
        }

        assert(tokens.pop() == "}")

    }

    fun readKeyValue(tokens: LinkedList<String>): Pair<String, String> {
        val key = readValue(tokens)
        assert(tokens.pop() == ":")
        val value = readValue(tokens)
        return key to value
    }

    fun readValue(tokens: LinkedList<String>): String {
        val token = tokens.pop()
        if (token == "\"") {
            return tokens.pop().apply {
                assert(tokens.pop() == "\"")
            }
        } else {
            return token + " " + tokens.popWhile { it != ";" && it != ":" }.joinToString(" ")
        }
    }

}

inline fun <T> Deque<T>.popWhile(predicate: (T) -> Boolean): List<T> {
    val removed = mutableListOf<T>()
    while (predicate(peek())) {
        removed += pop()
    }
    return removed
}