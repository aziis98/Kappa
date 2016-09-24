package com.aziis98.kappa

import com.aziis98.kappa.control.ContainerWindow

// Copyright 2016 Antonio De Lucreziis

interface KappaApplication {
    fun setupWindow(containerWindow: ContainerWindow) {
        containerWindow.apply {
            containerWindow.setup()
        }
    }

    fun ContainerWindow.setup()
}

object Kappa {
    lateinit var args: Array<String>

    fun launch(instance: KappaApplication, args: Array<String>) {
        println("Launching ${instance.javaClass.simpleName}")

        Kappa.args = args

        val containerWindow = ContainerWindow()
        val windowHandle = createWindow(containerWindow)

        instance.setupWindow(containerWindow)
    }

    object Controls {
        // TODO
    }

    object Time {
        fun milliTime(): Long {
            return System.nanoTime() / 1000000
        }
    }

    object Properties {
        inline fun <reified R> register(propertyFactory: PropertyFactory<R>) {
            Property.register<R>(propertyFactory)
        }
    }
}

