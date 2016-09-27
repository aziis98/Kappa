package com.aziis98.control

import com.aziis98.kappa.Kappa
import com.aziis98.kappa.WindowHandle
import com.aziis98.utils.drawCenteredString
import java.awt.Font
import java.awt.Graphics2D
import java.util.*

/**
 * Created by aziis98 on 27/09/2016.
 */
open class TextControl(handle: WindowHandle, parent: Control, id: String) : ChildControl(handle, parent, id) {

    enum class WrapMode {
        Centered, Block, Left, Right
    }

    var text = ""
    var font = Kappa.Constants.defaultFont

    var wrapMode = WrapMode.Centered

    override fun render(g: Graphics2D) {
        super.render(g)

        clipGraphics(g)

        g.translate(x, y)

        val bounds = g.fontMetrics.getStringBounds(text, g)
        val lines = text.split("\n")
        val lineHeight = bounds.height.toInt()

        when (wrapMode) {
            TextControl.WrapMode.Centered -> {
                lines.forEachIndexed { i, line ->
                    g.drawCenteredString(line,
                            width / 2,
                            (height - lineHeight * lines.size) / 2 + i * lineHeight
                    )
                }
            }
            TextControl.WrapMode.Block -> TODO()
            TextControl.WrapMode.Left -> TODO()
            TextControl.WrapMode.Right -> TODO()
        }

        g.translate(-x, -y)

        g.clip = null
    }

}