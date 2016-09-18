package com.aziis98.utils


// Copyright 2016 Antonio De Lucreziis

// const val validSymbols = "$'+-*/^<>=?_.%@"

// fun Char.isValidChar() = isLetter() || validSymbols.indexOf(this) != -1


fun stdGluer(a: Char, b: Char): Boolean =
    (a.isWhitespace() && b.isWhitespace())
        || (a.isDigit() && b.isDigit())
        || ((a.isDigit() && b == '.')
            || (a == '.' && b.isDigit()))

fun tokenize(source: CharArray, gluer: (Char, Char) -> Boolean = ::stdGluer): List<CharArray> {

    val list = mutableListOf<CharArray>()

    val sourceList = source.toList()

    val front = sourceList.take(source.size - 1)
    val back = sourceList.takeLast(source.size - 1)

    val zipped = front.zip(back).toMutableList()

    val sb = StringBuilder()
    while (zipped.isNotEmpty()) {
        val entry = zipped.removeAt(0)

        sb.append(entry.first)

        val glueFlag = gluer(entry.first, entry.second)

        if (!glueFlag) {
            list.add(sb.toString().toCharArray())
            sb.setLength(0)
        }

        if (zipped.isEmpty()) {
            sb.append(entry.second)

            list.add(sb.toString().toCharArray())
        }
    }


    return list
}