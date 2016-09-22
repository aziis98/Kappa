package com.aziis98.kappa.parser

import com.aziis98.kappa.structure.*
import com.aziis98.utils.tokenize
import java.nio.file.*
import java.util.*

// Copyright 2016 Antonio De Lucreziis
abstract class AbstractParser {

    fun parse(path: Path, globalContext: LinearGraph<String, String> = LinearGraph<String, String>()): LinearGraph<String, String> {
        val fileName = path.fileName.toString()
        val tokens = tokenize(path.toFile().readText().toCharArray()) { a, b -> gluer(a, b) }.map { String(it) }
        val local = globalContext.putPair(fileName)
        val linkedList = LinkedList(refineTokens(tokens))

        parse(linkedList, local)

        return globalContext
    }

    protected abstract fun gluer(a: Char, b: Char): Boolean

    protected abstract fun refineTokens(tokens: List<String>): List<String>

    protected abstract fun parse(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode)

}