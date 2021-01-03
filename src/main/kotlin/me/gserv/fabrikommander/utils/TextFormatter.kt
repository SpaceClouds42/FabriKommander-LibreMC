package me.gserv.fabrikommander.utils

fun formatString(text: String): String {
    return customVariables((minecraftFormatting(text)))
}

fun minecraftFormatting(text: String): String {
    return text
        .replace("""(?<!\\)&(?=([0123456789abcdefklmnor]))""".toRegex(), "§")
        .replace("""\\(?=(&))""".toRegex(), "")
}

fun customVariables(text: String): String {
    return text
        .replace("#TPS".toRegex(), "%.2f".format(TablistVariables.INSTANCE.getTPS()))
        .replace("#MSPT".toRegex(), "%.2f".format(TablistVariables.INSTANCE.mspt))
        .replace("#UPTIME".toRegex(), TablistVariables.INSTANCE.getUptime())
        .replace("#N".toRegex(), "\n")
}