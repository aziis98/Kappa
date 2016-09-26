package com.aziis98.utils

/**
 * Created by aziis98 on 26/09/2016.
 */

fun <T> Boolean.choose(ifTrue: T, ifFalse: T) = if (this) ifTrue else ifFalse