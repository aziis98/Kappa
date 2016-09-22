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

    inline fun <reified R : KappaApplication> launch(args: Array<String>) {
        println("Launching ${R::class.simpleName}")
        val instance = R::class.objectInstance
                ?: error("The control ${R::class.simpleName} has no object instance!")

        Kappa.args = args

        val windowHandle = createWindow()
        val containerWindow = ContainerWindow()

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

