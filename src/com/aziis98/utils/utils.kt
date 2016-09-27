package com.aziis98.utils

import java.awt.Graphics2D

/**
 * Created by aziis98 on 26/09/2016.
 */

fun <T> Boolean.choose(ifTrue: T, ifFalse: T) = if (this) ifTrue else ifFalse

fun Graphics2D.drawCenteredString(string: String, x: Int, y: Int) {
    val bounds = fontMetrics.getStringBounds(string, this)

    drawString(string, x - (bounds.width / 2).toInt(), y + (bounds.height / 2).toInt())
}


