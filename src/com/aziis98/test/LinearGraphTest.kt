package com.aziis98.test

import com.aziis98.kappa.structure.*
import org.junit.Test

// Copyright 2016 Antonio De Lucreziis

class LinearGraphTest {

    fun getDummyGraph() : LinearGraph<String, String> {
        val graph = LinearGraph<String, String>()

        graph.putPair("VE").apply {
            put("Antonio", "Teismo")
            put("Riccardo", "Cristianesimo")
            put("Sara", "Ateismo")
            put("Andrea", "Caffé-ismo")
        }

        graph.putPair("Profs").apply {
            put("Ricciardi", "Cristianesimo")
            put("Carta", "Ateismo")
        }

        return graph
    }

    @Test
    fun testLinearGraph() {

    }

}