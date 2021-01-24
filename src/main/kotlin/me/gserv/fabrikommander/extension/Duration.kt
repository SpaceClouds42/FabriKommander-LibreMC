package me.gserv.fabrikommander.extension

import java.time.Duration

fun Duration.prettyPrint(): String {
    val hmsArray = this.toString()
        .substring(2)
        .replace("(\\d[HMS])(?!$)".toRegex(), "$1 ")
        .replace("\\.\\d+".toRegex(), "")
        .toLowerCase()
        .split(" ")

    var hours = 0
    var minutes = 0
    var seconds = 0

    for (e in hmsArray) {
        if (e.endsWith("h")) { hours   = e.replace("h", "").toInt() }
        if (e.endsWith("m")) { minutes = e.replace("m", "").toInt() }
        if (e.endsWith("s")) { seconds = e.replace("s", "").toInt() }
    }

    val days = hours/24
    hours -= (days * 24)

    var prettyString = ""

    if (days == 1) {
        prettyString += "1 day"
    } else if (days > 0) {
        prettyString += "$days days"
    }

    if (hours == 1) {
        prettyString += " 1 hour"
    } else if (hours > 0) {
        prettyString += " $hours hours"
    }

    if (minutes == 1) {
        prettyString += " 1 minute"
    } else if (minutes > 0) {
        prettyString += " $minutes minutes"
    }

    if (seconds == 1) {
        prettyString += " 1 second"
    } else if (seconds > 0) {
        prettyString += " $seconds seconds"
    }

    return prettyString
}