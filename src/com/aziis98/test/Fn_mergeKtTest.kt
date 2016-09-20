package com.aziis98.test

import com.aziis98.utils.*
import org.junit.Test

/**
 * Created by aziis98 on 19/09/2016.
 */
class Fn_mergeKtTest {
    @Test
    fun test() {
        val list = tokenize("prova, non123 \" test \" /*a comment*/".toCharArray()).map { String(it) }

        println(list.map { "[$it]" }.joinToString(" "))

        /*
        val merged = list.merge({ it.joinToString("") }, { state, element ->
            when {
                state == MergeState.None && (
                        element == "\"" || element == "/*") -> MergeState.Open
                state == MergeState.Continue
                        && (element == "\"" || element == "*/") -> MergeState.Close
                else -> state
            }
        })
        */

        val merged2 = list.merge( {it.joinToString("") }, presetsOf(
                testOf("/*") to testOf("*/"),
                testOf("\"") to testOf("\"")
        ))

        println(merged2.map { "[$it]" }.joinToString(" "))
    }
}