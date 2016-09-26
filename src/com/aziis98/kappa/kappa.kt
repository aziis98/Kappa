package com.aziis98.kappa

import com.aziis98.control.Window
import java.awt.Graphics2D

// Copyright 2016 Antonio De Lucreziis

abstract class KappaApplication {
    val windowHandle by lazy { createWindow(this) }
    val window by lazy { Window(windowHandle) }

    abstract fun setup()

    abstract fun draw(g: Graphics2D)
}

object Kappa {
    lateinit var args: Array<String>

    fun launch(instance: KappaApplication, args: Array<String>) {
        println("Launching ${instance.javaClass.simpleName}")

        Kappa.args = args

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

    object Properties {

    }
}

