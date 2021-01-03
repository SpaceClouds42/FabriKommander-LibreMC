package me.gserv.fabrikommander.utils

fun formatString(text: String): String {
    return text
        .replace("""(?<!\\)&(?=([0123456789abcdefklmnor]))""".toRegex(), "§")
        .replace("""\\(?=(&))""".toRegex(), "")
}

fun tablistChars(text: String): String {
    return text
        .replace("#TPS".toRegex(), TablistVariables().getTPS().toString())
        .replace("#MSPT".toRegex(), TablistVariables().mspt.toString())
        .replace("#UPTIME".toRegex(), TablistVariables().getUptime())
        .replace("#N".toRegex(), "\n")
}