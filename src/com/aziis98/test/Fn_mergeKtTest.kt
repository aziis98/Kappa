package com.aziis98.test

import com.aziis98.utils.*
import org.junit.Test

/**
 * Created by aziis98 on 19/09/2016.
 */
class Fn_mergeKtTest {
    @Test
    fun test() {
        val list = tokenize((
                "prova, non123 \" test" +
                " \" /*a co \" mment*/" +
                "testing | prova 1 or 2 but | to 2 by 1").toCharArray()).map { String(it) }

        println(list.map { "[$it]" }.joinToString(" "))

        val merged2 = list.merge( {it.joinToString("") }, templatesOf(
                templateOf("string", "\"", "\""),
                templateOf("comment", "/*", "*/"),
                templateOf("pipe1", "|", "1"),
                templateOf("pipe1", "|", "2")
        ))

        println(merged2.map { "[$it]" }.joinToString(" "))
    }
}