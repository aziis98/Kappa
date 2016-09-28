package com.aziis98.kappa.control

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

fun <C : Container> C.distributeHorizontal() {
    children.forEachIndexed { i, control ->
        control.x = i * this.width / children.size
        control.width = this.width / children.size
    }
}

fun <C : Container> C.distributeVertical() {
    children.forEachIndexed { i, control ->
        control.y = i * this.height / children.size
        control.height = this.height / children.size
    }
}

fun <C : Container> C.stackHorizontal(width: Int) {
    children.forEachIndexed { i, control ->
        control.x = i * (width + 1)
        control.width = width
    }
}

fun <C : Container> C.stackVertical(height: Int) {
    children.forEachIndexed { i, control ->
        control.y = i * (height + 1)
        control.height = height
    }
}