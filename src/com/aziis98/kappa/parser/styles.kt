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
    }

    override fun parse(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {



    }

    fun parseObject(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {

        val name = tokens.pop()

        assert(tokens.pop() == "{")

        while (tokens.peek() != "}") {

        }

        assert(tokens.pop() == "}")

    }

    fun parseKeyValue() {

    }



}