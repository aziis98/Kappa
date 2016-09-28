package com.aziis98.kappa.control

import com.aziis98.kappa.WindowHandle
import java.awt.event.KeyEvent

/**
 * Created by aziis98 on 28/09/2016.
 */
class InputTextControl(handle: WindowHandle, parent: Control, id: String) : TextControl(handle, parent, id) {

    var cursor = 0
    var focussed = false

    init {
        dynamic {
            if (handle.keyboard.hasPendingEvent) {
                val lastKey = handle.keyboard.lastKey
                val lastChar = handle.keyboard.lastChar

                when (lastKey) {
                    KeyEvent.VK_BACK_SPACE -> {
                        text = text.substring(0, text.length - 2)
                    }
                    KeyEvent.VK_LEFT -> cursor--
                    KeyEvent.VK_RIGHT -> cursor++
                    else -> {
                        text += lastChar
                    }
                }
            }
        }
    }

}