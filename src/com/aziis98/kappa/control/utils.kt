package com.aziis98.kappa.control

/**
 * Created by aziis98 on 22/09/2016.
 */

data class ConstraintPosition(val width: Int = ABSENT,
                              val height: Int = ABSENT,
                              val top: Int = ABSENT,
                              val right: Int = ABSENT,
                              val bottom: Int = ABSENT,
                              val left: Int = ABSENT)

data class Rectangle(val x: Int,
                     val y: Int,
                     val width: Int,
                     val height: Int)

fun computeBySides(container: Rectangle, constraint: ConstraintPosition): Rectangle {
    fun Int.isPresent() = this != ABSENT

    val px = when {
        constraint.left.isPresent() -> {
            constraint.left
        }
        constraint.right.isPresent() -> {
            container.width - (constraint.width + constraint.right)
        }
        else -> error("left and right were absent")
    }

    val py = when {
        constraint.top.isPresent() -> {
            constraint.top
        }
        constraint.bottom.isPresent() -> {
            container.height - (constraint.height + constraint.bottom)
        }
        else -> error("top and bottom were absent")
    }

    return Rectangle(px, py, constraint.width, constraint.height)
}