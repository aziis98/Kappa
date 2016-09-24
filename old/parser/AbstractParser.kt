package com.aziis98.kappa.parser

import com.aziis98.kappa.structure.*
import com.aziis98.utils.tokenize
import java.nio.file.*
import java.util.*

interface IContext

// Copyright 2016 Antonio De Lucreziis
abstract class AbstractParser<T : IContext> {

    fun parse(path: Path): T {
        val fileName = path.fileName.toString()
        val tokens = tokenize(path.toFile().readText().toCharArray()) { a, b -> gluer(a, b) }.map { String(it) }

        val linkedList = LinkedList(refineTokens(tokens))

        return parse(linkedList)
    }

    protected abstract fun gluer(a: Char, b: Char): Boolean

    protected abstract fun refineTokens(tokens: List<String>): List<String>

    protected abstract fun parse(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode)

}