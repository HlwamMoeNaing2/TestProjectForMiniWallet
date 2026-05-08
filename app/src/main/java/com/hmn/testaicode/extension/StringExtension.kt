package com.hmn.testaicode.extension

fun String.isValidPhone(): Boolean {

    val trimmed = this.trim()

    return when {

        trimmed.length < 8 -> false

        trimmed.length > 9 -> false

        !trimmed.all { it.isDigit() } -> false

        else -> true

    }

}