package com.aziis98.control

import java.awt.Color
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.defaultType
import kotlin.reflect.jvm.javaType

/**
 * Created by aziis98 on 25/09/2016.
 */

interface ValueConverter<T> {
    fun read(source: String): T
    fun encode(value: T): String
}

class StyleMap {
    companion object {
        val converters = HashMap<String, ValueConverter<*>>()

        init {
            register(converterInt)
            register(converterDouble)
            register(converterString)
            register(converterColor)
        }

        inline fun <reified R> register(valueConverter: ValueConverter<R>) {
            converters.put(R::class.defaultType.javaType.typeName, valueConverter)
        }
    }

    private val map
            = HashMap<String, String>()

    operator fun get(key: String) = map[key] ?: error("No element with that name")

    operator fun set(key: String, value: String) {
        map[key] = value
    }

    inline fun <reified R> get(key: String)
            = (converters[R::class.defaultType.javaType.typeName]?.read(this[key]) ?: error("No element with that type")) as R

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, kType: KType): T {
        return (converters[kType.javaType.typeName]?.read(this[key]) ?: error("No element with that type")) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> set(key: String, value: T, kType: KType) {
        this[key] = (converters[kType.javaType.typeName] as ValueConverter<T>).encode(value)
    }

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified R> set(key: String, value: R) {
        this[key] = (converters[R::class.defaultType.javaType.typeName] as ValueConverter<R>).encode(value)
    }

}

internal val converterInt = object : ValueConverter<Int> {
    override fun read(source: String) = source.toInt()
    override fun encode(value: Int) = value.toString()
}
internal val converterDouble = object : ValueConverter<Double> {
    override fun read(source: String) = source.toDouble()
    override fun encode(value: Double) = value.toString()
}
internal val converterString = object : ValueConverter<String> {
    override fun read(source: String) = source
    override fun encode(value: String) = value
}
internal val converterColor = object : ValueConverter<Color> {
    override fun read(source: String)
            = Color(Integer.parseInt(source.substring(1), 16))
    override fun encode(value: Color) = '#'+
            Integer.toHexString(value.red) +
            Integer.toHexString(value.green) +
            Integer.toHexString(value.blue)
}

class StyleMapDelegate<T : Any>(val key: String) {
    operator fun getValue(thisRef: Control, property: KProperty<*>): T {
        println(property.returnType)

        return thisRef.style.get<T>(key, property.returnType)
    }

    operator fun setValue(thisRef: Control, property: KProperty<*>, value: T) {
        thisRef.style.set(key, value, property.returnType)
    }
}

fun <T : Any> property(key: String) = StyleMapDelegate<T>(key)