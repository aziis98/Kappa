package com.aziis98.control

import com.aziis98.kappa.WindowHandle
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

/**
 * Created by aziis98 on 24/09/2016.
 */

inline fun <C : Control> C.dynamic(crossinline action: (C) -> Unit) {
    this.dynamics.add(object : IDynamic {
        @Suppress("UNCHECKED_CAST")
        override fun update(control: Control) {
            action(control as C)
        }
    })
}

interface IDynamic {
    fun update(control: Control)
}

open class Control(val id: String) {

    var dynamics = ArrayList<IDynamic>()

    var backgroundColor: Color = Color.WHITE
    var borderColor: Color = Color.BLACK

    var visible: Boolean = true

    var x: Int = -1
    var y: Int = -1
    var width: Int = -1
    var height: Int = -1

    var borderRadius: Int = 0

    private fun updateDynamics() {
        dynamics.forEach {
            it.update(this)
        }
    }

    open fun render(g: Graphics2D) {
        updateDynamics()

        if (!visible || listOf(x, y, width, height).all { it == -1 }) return

        g.color = backgroundColor
        g.fillRoundRect(x, y, width, height, borderRadius, borderRadius)

        g.color = borderColor
        g.drawRoundRect(x, y, width, height, borderRadius, borderRadius)
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

    fun append(control: ChildControl) {
        children.add(control)
    }
}

open class WindowControl(val handle: WindowHandle) : Control("window") {
    val rootContainer = Container(this, "root")

    init {
        x = 0
        y = 0
    }

    fun update() {
        width = handle.windowWidth
        height = handle.windowHeight
    }

    override fun render(g: Graphics2D) {
        update()

        super.render(g)

        rootContainer.render(g)
    }
}

inline fun <reified R : ChildControl> Container.appendReflective(id: String): R {
    val newControl = R::class.constructors.first().call(this, id)
    append(newControl)
    return newControl
}