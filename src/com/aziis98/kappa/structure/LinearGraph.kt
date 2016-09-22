package com.aziis98.kappa.structure

import java.util.*

// Copyright 2016 Antonio De Lucreziis

class LinearGraph<K, V> {

    val rootLength: Int
        get() = rootValues.size

    val rootValues = hashMapOf<K, GraphNode>()
    val values = arrayListOf<GraphNode>()
    internal val arrows = hashMapOf<GraphArrow<K, V>, GraphNode>()

    fun put(key: K, value: V) = create(value, key)
    fun put(value: V) = create(value)

    fun create(value: V, key: K? = null): GraphNode {
        return GraphNode(value).apply {
            if (key != null) {
                rootValues.put(key, this)
            }
            values.add(this)
        }
    }

    fun link(origin: GraphNode, key: K, target: GraphNode) {
        arrows.put(GraphArrow(origin, key), target)
    }

    fun getByOrigin(origin: GraphNode, key: K): V? {
        return arrows[GraphArrow(origin, key)]?.value
    }

    fun getRoot(rootKey: K) : GraphNode? {
        return rootValues[rootKey] ?: error("No such element linked to that key: rootKey= $rootKey")
    }

    inner class GraphNode(val value: V) {
        fun put(key: K, value: V): GraphNode {
            val target = create(value)
            link(this, key, target)
            return target
        }

        operator fun get(key: K) = getByOrigin(this, key)

        override fun toString(): String {
            return "GraphNode(value = $value, hash = ${hashCode()})"
        }
    }

    data class GraphArrow<out K, V>(val origin: GraphNode, val key: K)

    override fun toString(): String {
        return "LinearGraph(\n  ${arrows.map { "${it.key.origin.value} -> ${it.key.key} : ${it.value.value}" }.joinToString("\n  ")}\n)"
    }

}

inline fun <T> LinearGraph<*, T>.forEachRoot(fn: (LinearGraph<*, T>.GraphNode) -> Unit) {
    rootValues.forEach {
        fn(it.value)
    }
}

inline fun <T> LinearGraph<*, T>.forEach(fn: (LinearGraph<*, T>.GraphNode) -> Unit) {
    values.forEach { fn(it) }
}

fun <T> LinearGraph<T, T>.putPair(keyValue: T) = put(keyValue, keyValue)