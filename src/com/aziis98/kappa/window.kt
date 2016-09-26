package com.aziis98.kappa

import java.awt.*
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

    val windowWidth: Int
        get() = (jFrame.width / resolutionFactor).toInt()
    val windowHeight: Int
        get() = (jFrame.height / resolutionFactor).toInt()

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
            prevX = 0
            x = 0
            prevY = 0
            y = 0
        }

        init {
            jPanel.addMouseListener(this)
            jPanel.addMouseMotionListener(this)
            jPanel.addMouseWheelListener(this)
        }
    }
}

fun createWindow(instance: KappaApplication): WindowHandle {

    val jframe = JFrame("<untitled>")
    jframe.setLocationRelativeTo(null)
    jframe.isResizable = true
    jframe.setSize(1000, 600)
    jframe.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

    val jPanel = object : JPanel(true) {
        override fun paintComponent(g: Graphics) {
            g as Graphics2D

            val resolutionFactor = Toolkit.getDefaultToolkit().screenResolution * 0.01

            g.scale(resolutionFactor, resolutionFactor)

            g.background = Color.BLACK
            g.clearRect(0, 0, jframe.width, jframe.height)

            instance.draw(g)

            repaint()
        }
    }

    jframe.contentPane.add(jPanel)

    return WindowHandle(jframe, jPanel).apply {
        jframe.isVisible = true
    }
}
