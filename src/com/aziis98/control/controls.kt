package com.aziis98.control

import java.awt.Color
import java.awt.Graphics2D

/**
 * Created by aziis98 on 24/09/2016.
 */

open class Control(val id: String) {
    val style = StyleMap()

    var x: Int by property("x")
    var y: Int by property("y")

    init {
        x = 0
        y = 0

        style["width"] = 0
        style["height"] = 0

        style["background"] = Color.WHITE
        style["border-color"] = Color.BLACK
    }

    fun render(g: Graphics2D) {
        val width = style.get<Int>("width")
        val height = style.get<Int>("height")

        val background = style.get<Color>("background")
        val borderColor = style.get<Color>("border-color")

        g.color = background
        g.fillRect(x, y, width, height)

        g.color = borderColor
        g.drawRect(x, y, width, height)
    }
}