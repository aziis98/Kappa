package com.aziis98.control

import com.aziis98.kappa.WindowHandle
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

/**
 * Created by aziis98 on 24/09/2016.
 */

open class Control(val id: String) {
    var backgroundColor: Color = Color.WHITE
    var borderColor: Color = Color.BLACK

    var visible: Boolean = true

    var x: Int = -1
    var y: Int = -1
    var width: Int = -1
    var height: Int = -1

    open fun render(g: Graphics2D) {
        if (listOf(x, y, width, height).all { it == -1 }) return

        g.color = backgroundColor
        g.fillRect(x, y, width, height)

        g.color = borderColor
        g.drawRect(x, y, width, height)
    }
}

open class ChildControl(val parent: Control, id: String) : Control(id)

open class Container(parent: Control, id: String) : ChildControl(parent, id) {
    val children = ArrayList<ChildControl>()

    override fun render(g: Graphics2D) {
        super.render(g)

        children.forEach {
            it.render(g)
        }
    }
}

open class Window(val windowHandle: WindowHandle) : Control("window") {
    val rootContainer = Container(this, "root")

    override fun render(g: Graphics2D) {
        super.render(g)
    }
}