package com.aziis98.kappa

import java.awt.Color
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by aziis98 on 22/09/2016.
 */

interface PropertyFactory<T> {
    fun read(string: String): Property<T>
}

interface PropertyListener<T> {
    fun onSet(oldValue: T, newValue: T): Boolean
}

fun <T> propertyOf(value: T) = Property(value)

class Property<T>(value: T) {
    companion object {
        val propertyTypes = HashMap<KClass<*>, PropertyFactory<*>>()

        init {
            register<Int> {
                propertyOf(it.toInt())
            }
            register<Double> {
                propertyOf(it.toDouble())
            }
            register<Color> {
                val intColor = Integer.parseInt(it.substring(1), 16)

                propertyOf(Color(intColor))
            }
            register<String> {
                propertyOf(it)
            }
        }

        @Suppress("UNCHECKED_CAST")
        inline fun <reified R> read(string: String): Property<R> {
            return propertyTypes[R::class]!!.read(string) as Property<R>
        }

        inline fun <reified R> register(propertyFactory: PropertyFactory<R>) {
            propertyTypes.put(R::class, propertyFactory)
        }

        inline fun <reified R> register(crossinline readFunction: (String) -> Property<R>) {
            return register(object : PropertyFactory<R> {
                override fun read(string: String): Property<R> {
                    return readFunction(string)
                }
            })
        }
    }

    val listeners = LinkedList<PropertyListener<T>>()

    var value: T = value
        get() = field
        set(value) {
            val oldValue = value
            if (listeners.all { it.onSet(oldValue, field) }) {
                field = oldValue
            }
        }
}