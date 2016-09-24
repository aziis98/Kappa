package com.aziis98.kappa.control

import com.aziis98.kappa.WindowHandle
import com.aziis98.kappa.parser.ParserCSS
import com.aziis98.kappa.structure.LinearGraph
import com.aziis98.kappa.structure.forEachRoot
import java.awt.Graphics2D
import java.nio.file.Path

/**
 * Created by aziis98 on 22/09/2016.
 */

class ContainerWindow() : Container(null, "window") {
    val stylesObject = LinearGraph<String, String>()

    init {
        style.top.value = 0
        style.right.value = 0
        style.bottom.value = 0
        style.left.value = 0
    }

    fun loadStyle(path: Path) {
        ParserCSS.parse(path, stylesObject)
    }

    fun updateStyles() {
        stylesObject.forEachRoot {
            it.nodes().forEach {
                println("$it")

                val elements = queryElements(it.key)

                it.value.nodes().forEach { prop ->
                    elements.forEach {
                        it.style[prop.key] = prop.value
                    }
                }
            }
        }
    }

    override fun renderRoot(g: Graphics2D, windowHandle: WindowHandle) {
        style.width.value = windowHandle.jPanel.width
        style.height.value = windowHandle.jPanel.height

        renderChildren(g)
    }
}

fun <T : Control> Container.control(control: T, init: T.() -> Unit) {
    control.init()
    append(control)
}

fun Container.container(id: String, init: Container.() -> Unit)
        = this.control(Container(this, id), init)