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
    val prefix = rankToPrefix[PlayerDataManager.getRank(player.uuid)]!!.shallowCopy()
    val name = reset("") +
                hover(
                    click(
                        gray(player.entityName),
                        ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            "/tell ${player.entityName}"
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        white(player.entityName +
                                "\nType: Player\n" +
                                player.uuidAsString
                        )
                    )
                )
                gray(player.entityName) +
                reset("")
    val connector = reset(" ") + darkGray(bold(">")) + reset(" ")
    val messageAsMutableText = reset(white(message))
    return prefix + name + connector + messageAsMutableText
}