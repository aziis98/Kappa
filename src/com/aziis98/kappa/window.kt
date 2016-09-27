package com.aziis98.kappa

import com.aziis98.utils.fixJFrameAtCenter
import com.sun.org.apache.xpath.internal.operations.Bool
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * Created by aziis98 on 22/09/2016.
 */

data class WindowHandle(val jFrame: JFrame, val jPanel: JPanel) {
    var title: String
        get() = jFrame.title
        set(value) {
            jFrame.title = value
        }

    var windowWidth: Int
        get() = (jFrame.width / resolutionFactor).toInt()
        set(value) {
            jFrame.setSize(
                    (value * resolutionFactor).toInt(),
                    jFrame.height
            )
        }

    var windowHeight: Int
        get() = (jFrame.height / resolutionFactor).toInt()
        set(value) {
            jFrame.setSize(
                    jFrame.width,
                    (value * resolutionFactor).toInt()
            )
        }

    var resizable: Boolean
        get() = jFrame.isResizable
        set(value) {
            jFrame.isResizable = value
        }

    val resolutionFactor by lazy { Toolkit.getDefaultToolkit().screenResolution * 0.01 }

    var visible: Boolean
        get() = jFrame.isVisible
        set(value) {
            jFrame.isVisible = value
        }

    val mouse = Mouse()
    val keyboard = Keyboard()

    inner class Mouse : MouseAdapter() {
        var x = 0
        var y = 0

        var prevX = 0
        var prevY = 0

        var cursor = Cursor.DEFAULT_CURSOR

        override fun mouseMoved(e: MouseEvent) {
            prevX = x
            prevY = y

            x = (e.x / resolutionFactor).toInt()
            y = (e.y / resolutionFactor).toInt()

            jPanel.cursor = Cursor.getPredefinedCursor(cursor)
            cursor = Cursor.DEFAULT_CURSOR
        }

        override fun mouseExited(e: MouseEvent?) {
            prevX = -1
            x = -1
            prevY = -1
            y = -1
        }

        init {
            jPanel.addMouseListener(this)
            jPanel.addMouseMotionListener(this)
            jPanel.addMouseWheelListener(this)
        }
    }

    inner class Keyboard : KeyAdapter() {
        var lastKey = -1
        var lastChar = ' '
        var isKeyPressed = false
        var isKeyPressedPrev = false

        var keyCooldown = 0
        val isEvent: Boolean
            get() = keyCooldown > 0

        fun updateKeyEvents() {
            if (isKeyPressed != isKeyPressedPrev) {
                keyCooldown = Kappa.Constants.keyCooldown
            } else {
                if (keyCooldown > 0) keyCooldown--
            }
        }

        override fun keyPressed(e: KeyEvent) {
            lastKey = e.keyCode
            isKeyPressedPrev = isKeyPressed
            isKeyPressed = true
        }
        override fun keyReleased(e: KeyEvent) {
            lastKey = e.keyCode
            isKeyPressedPrev = isKeyPressed
            isKeyPressed = false
        }
        override fun keyTyped(e: KeyEvent) {
            lastKey = e.keyCode
            lastChar = e.keyChar
            isKeyPressedPrev = isKeyPressed
            isKeyPressed = true
        }
    }
}

fun createWindow(instance: KappaApplication): WindowHandle {

    val jframe = JFrame("<untitled>")
    jframe.setLocationRelativeTo(null)
    jframe.isResizable = true
    jframe.setSize(
            (Kappa.Constants.resolutionFactor * instance.preferedWindowWidth).toInt(),
            (Kappa.Constants.resolutionFactor * instance.preferedWindowHeight).toInt()
    )
    jframe.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

    val jPanel = object : JPanel(true) {
        override fun paintComponent(g: Graphics) {
            g as Graphics2D

            g.scale(Kappa.Constants.resolutionFactor, Kappa.Constants.resolutionFactor)

            g.background = Color.BLACK
            g.clearRect(0, 0, jframe.width, jframe.height)

            instance.draw(g)

            repaint()
        }
    }

    jframe.contentPane.add(jPanel)

    return WindowHandle(jframe, jPanel).apply {
        jframe.isVisible = true
        fixJFrameAtCenter(jframe)
    }
}
