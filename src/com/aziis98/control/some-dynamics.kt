package com.aziis98.control

/**
 * Created by aziis98 on 26/09/2016.
 */


fun <C : Control> C.appendHoverDynamic() = dynamic {
    hover = (handle.mouse.x in absoluteX..absoluteX + width) && (handle.mouse.y in absoluteY..absoluteY + height)
}