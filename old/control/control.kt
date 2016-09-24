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

    private val nameMap = mapOf<String, Property<*>>(
            "background" to background,
            "border-color" to borderColor,
            "color" to color
    )

    @Suppress("UNCHECKED_CAST")
    operator fun <T> set(propName: String, value: T) {
        (nameMap[propName] as Property<T>).value = value
    }
}

const val ABSENT = -1
fun Property<Int>.isPresent() = this.value != ABSENT

open class Control(val parent: Control?, val id: String) {

    val tags = arrayListOf<String>()
    val style = ControlStyle()

    open fun render(g: Graphics2D) {
        val rect = computeBySides(
                parent?.rectangle ?: error("User renderRoot() for the root control!"),
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

    val absoluteLeft: Int
        get() = if (parent == null) 0 else parent.absoluteLeft + style.left.value

    val absoluteTop: Int
        get() = if (parent == null) 0 else parent.absoluteTop + style.top.value

    val rectangle: Rectangle
        get() = Rectangle(0, 0, style.width.value, style.height.value)

    val absoluteRectangle: Rectangle
        get() = rectangle.copy(x = absoluteLeft, y = absoluteTop)
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

fun queryPredicate(query: String): (Control) -> Boolean {
    val hasId = !query.startsWith(".")
    var id = ""
    var tagQuery = query

    if (hasId) {
        val dotIndex = query.indexOf(".")

        if (dotIndex == -1) {
            id = query.substring(0, query.length)
            tagQuery = ""
        }
        else {
            id = query.substring(0, dotIndex)
            tagQuery = query.substring(dotIndex)
        }

    }

    val tags = tagQuery.split(".").filter { !it.isBlank() }

    println("[tags] $tagQuery -> $tags")

    return {
        (!hasId || it.id == id) && it.tags.containsAll(tags)
    }
}

fun Container.queryElements(query: String)
    = queryElements(query.split(" "))

fun Container.queryElements(query: List<String>) : List<Control> {
    val cLevelQuery = childern.values.filter(queryPredicate(query.first()))
    if (query.size == 1) {
        return cLevelQuery
    } else {
        return cLevelQuery.flatMap {
            if(it is Container)
                it.queryElements(query.takeLast(query.size - 1))
            else
                listOf(it)
        }
    }
}