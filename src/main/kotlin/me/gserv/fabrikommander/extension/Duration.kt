package me.gserv.fabrikommander.extension

import java.time.Duration

fun Duration.prettyPrint(): String {
    val fancy = this.toString()
        .substring(2)
        .replace("(\\d[HMS])(?!$)".toRegex(), "$1 ")
        .replace("\\.\\d+".toRegex(), "")
        .toLowerCase()
    //TODO: replace h, m, s with day(s), hour(s), minute(s), second(s)
    return fancy
}