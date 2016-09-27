package com.aziis98.kappa

import com.aziis98.control.WindowControl
import com.aziis98.control.dynamic
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Toolkit

// Copyright 2016 Antonio De Lucreziis

abstract class KappaApplication {
    val handle = createWindow(this)
    val window = WindowControl(handle)

    abstract fun setup()

    abstract fun draw(g: Graphics2D)

    open val preferedWindowWidth: Int = 640
    open val preferedWindowHeight: Int = 480
}

object Kappa {
    lateinit var args: Array<String>

    fun launch(instance: KappaApplication, args: Array<String>) {
        println("Launching ${instance.javaClass.simpleName}")

        Kappa.args = args

        instance.window.dynamic { instance.window.handle.keyboard.updateKeyEvents() }

        instance.setup()
    }

    object Controls {
        // TODO
    }

    object Time {
        fun milliTime(): Long {
            return System.nanoTime() / 1000000
        }

        fun currentSeconds(): Double = milliTime() / 1000.0
    }

    object Constants {
        val defaultFont = Font("Segoe UI", Font.PLAIN, 15)
        val resolutionFactor = Toolkit.getDefaultToolkit().screenResolution * 0.01

        var keyCooldown = 200
    }
}

