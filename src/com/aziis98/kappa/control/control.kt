package com.aziis98.kappa.control

import com.aziis98.kappa.Property
import com.aziis98.kappa.WindowHandle
import com.aziis98.kappa.propertyOf
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

/**
 * Created by aziis98 on 22/09/2016.
 */

class ControlStyle {
    val background = propertyOf<Color>(Color.WHITE)
    val color = propertyOf<Color>(Color.BLACK)

    val borderColor = propertyOf<Color>(Color.BLACK)
    val borderRadius = propertyOf(0)
    val borderSize = propertyOf(0)

    val height = propertyOf(ABSENT)
    val width = propertyOf(ABSENT)

    val top = propertyOf(ABSENT)
    val right = propertyOf(ABSENT)
    val bottom = propertyOf(ABSENT)
    val left = propertyOf(ABSENT)

    val hover = propertyOf(false)
    val mousePressed = propertyOf(false)
}

const val ABSENT = -1
fun Property<Int>.isPresent() = this.value != ABSENT

open class Control(val parent: Control?, val id: String) {

    val tags = arrayListOf<String>()
    val style = ControlStyle()

    open fun render(g: Graphics2D) {
        val rect = computeBySides(parent?.rectangle ?: error("User renderRoot() for the root control!"),
                ConstraintPosition(
                        width = style.width.value,
                        height = style.height.value,
                        left = style.left.value,
                        right = style.right.value,
                        top = style.top.value,
                        bottom = style.bottom.value
                ))

        g.color = style.background.value
        g.fillRoundRect(rect.x, rect.y, rect.width, rect.height,
                style.borderRadius.value,
                style.borderRadius.value)

        g.color = style.color.value
        g.stroke = BasicStroke(style.borderSize.value.toFloat())
        g.drawRoundRect(rect.x, rect.y, rect.width, rect.height,
                style.borderRadius.value,
                style.borderRadius.value)
    }

    open fun renderRoot(g: Graphics2D, windowHandle: WindowHandle) {
        TODO("Kind of useless in a non-container control")
    }

    val isRoot: Boolean
        get() = parent == null

    val rectangle: Rectangle
        get() = Rectangle(0, 0, style.width.value, style.height.value)
}

open class Container(parent: Control?, id: String) : Control(parent, id) {

    val childern = hashMapOf<String, Control>()

    override fun render(g: Graphics2D) {
        renderChildren(g)
        super.render(g)
    }

    fun renderChildren(g: Graphics2D) {
        g.translate(style.left.value, style.top.value)

        childern.forEach { id, control ->
            control.render(g)
        }

        g.translate(-style.left.value, -style.top.value)
    }

    fun append(control: Control) {
        childern.put(control.id, control)
    }
}