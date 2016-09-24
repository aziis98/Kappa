package com.aziis98.kappa.parser

import com.aziis98.kappa.structure.LinearGraph
import java.util.*

/**
 * Created by aziis98 on 24/09/2016.
 */
object ParserCss : AbstractParser() {
    override fun gluer(a: Char, b: Char): Boolean {
        return false
    }

    override fun refineTokens(tokens: List<String>): List<String> {

    }

    override fun parse(tokens: LinkedList<String>, context: LinearGraph<String, String>.GraphNode) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}