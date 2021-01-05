package me.gserv.fabrikommander.utils

import me.gserv.fabrikommander.data.PlayerDataManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText

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

fun formatChatMessage(player: ServerPlayerEntity, message: String): MutableText {
    val name = player.customName()
    val connector = reset(" ") + darkGray(bold(">")) + reset(" ")
    val messageAsMutableText = reset(white(message))
    return name + connector + messageAsMutableText
}