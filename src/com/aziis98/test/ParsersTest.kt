package com.aziis98.test

import com.aziis98.kappa.parser.ParserCSS
import org.junit.Test
import java.nio.file.Paths

/**
 * Created by aziis98 on 20/09/2016.
 */
class ParsersTest {
    @Test
    fun test() {
        println(ParserCSS.parse(Paths.get("res/test.css")))
    }
}