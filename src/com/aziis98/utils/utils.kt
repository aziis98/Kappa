package com.aziis98.utils

import java.awt.Graphics2D
import java.awt.Toolkit
import javax.swing.JFrame

/**
 * Created by aziis98 on 26/09/2016.
 */

fun <T> Boolean.choose(ifTrue: T, ifFalse: T) = if (this) ifTrue else ifFalse

fun Graphics2D.drawCenteredString(string: String, x: Int, y: Int) {
    val bounds = fontMetrics.getStringBounds(string, this)

    drawString(string, x - (bounds.width / 2).toInt(), y + (bounds.height / 2).toInt())
}

fun fixJFrameAtCenter(jFrame: JFrame) {
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    // jFrame.setLocation((screenSize.width - jFrame.width) / 2, (screenSize.height - jFrame.height) / 2)
    jFrame.setLocation((screenSize.width - jFrame.width) / 2, 150)
}
