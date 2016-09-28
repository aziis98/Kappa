package com.aziis98.test

import com.aziis98.kappa.Kappa
import com.aziis98.kappa.KappaApplication
import com.aziis98.kappa.control.InputTextControl
import com.aziis98.kappa.control.create
import com.aziis98.kappa.control.dynamic
import java.awt.Graphics2D

/**
 * Created by aziis98 on 28/09/2016.
 */

object KTest : KappaApplication() {
    override fun setup() {
        window.apply {
            handle.windowWidth = 600
            handle.windowHeight = 600

            rootContainer.apply {
                create<InputTextControl>("input") {
                    text = "<random text>"

                    dynamic {
                        x = 10
                        y = 10
                        width = 300
                        height = 30
                    }
                }
            }
        }
    }

    override fun draw(g: Graphics2D) {
        window.render(g)
    }
}

fun main(args: Array<String>) {
    Kappa.launch(KTest, args)
}