package com.aziis98.control

import com.aziis98.kappa.WindowHandle
import com.sun.org.apache.xpath.internal.operations.Bool
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

/**
 * Created by aziis98 on 24/09/2016.
 */

inline fun <C : Control> C.dynamic(crossinline action: C.() -> Unit) {
    this.dynamics.add(object : IDynamic {
        @Suppress("UNCHECKED_CAST")
        override fun update(control: Control) {
            (control as C).action()
        }
    })
}

interface IDynamic {
    fun update(control: Control)
}

open class Control(val handle: WindowHandle, val id: String) {

    var dynamics = ArrayList<IDynamic>()

    var backgroundColor: Color = Color.WHITE
    var borderColor: Color = Color.BLACK

    var visible: Boolean = true

    var x: Int = -1
    var y: Int = -1
    var width: Int = -1
    var height: Int = -1

    var borderRadius: Int = 0

    var hover: Boolean = false

    init {
        appendHoverDynamic()
    }

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

open class ChildControl(handle: WindowHandle, val parent: Control, id: String) : Control(handle, id)

open class Container(handle: WindowHandle, parent: Control, id: String) : ChildControl(handle, parent, id) {
    val children = ArrayList<ChildControl>()

    var directlyHover = false

    init {
        dynamic {
            directlyHover = hover && children.all { !it.hover }
        }
    }

    override fun render(g: Graphics2D) {
        super.render(g)

        clipGraphics(g)

        g.translate(x, y)
        children.forEach {
            it.render(g)
        }
        g.translate(-x, -y)

        g.clip = null
    }

    fun append(control: ChildControl) {
        children.add(control)
    }
}

open class WindowControl(handle: WindowHandle) : Control(handle, "window") {
    val rootContainer = Container(handle, this, "root")

    init {
        x = 0
        y = 0

        rootContainer.dynamic {
            positionSides(0, 0)
        }
    }

    fun update() {
        width = (handle.jPanel.width / handle.resolutionFactor).toInt()
        height = (handle.jPanel.height / handle.resolutionFactor).toInt()
    }

    override fun render(g: Graphics2D) {
        update()

        super.render(g)

        rootContainer.render(g)
    }
}

inline fun <reified R : ChildControl> Container.create(id: String, init: R.() -> Unit): R {
    val newControl = R::class.constructors.first().call(handle, this, id)
    append(newControl)
    return newControl.apply(init)
}

fun Control.clipGraphics(g: Graphics2D) {
    g.clipRect(x, y, width, height)
}