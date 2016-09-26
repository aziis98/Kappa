package com.aziis98.control

/**
 * Created by aziis98 on 26/09/2016.
 */

fun <C : ChildControl> C.positionSides(topBottom: Int = 0, leftRight: Int = 0)
        = positionSides(topBottom, leftRight, topBottom, leftRight)

fun <C : ChildControl> C.positionSides(top: Int = 0, left: Int = 0, bottom: Int = 0, right: Int = 0) {
    this.x = left
    this.y = top
    this.width = parent.width - left - right
    this.height = parent.height - top - bottom
}

fun <C : ChildControl> C.positionAbsolute(x: Int = 0, y: Int = 0, width: Int = parent.width, height: Int = parent.height) {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}