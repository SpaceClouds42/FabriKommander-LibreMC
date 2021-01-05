package me.gserv.fabrikommander.commands.text

import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.util.Util

class RanksCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("ranks")
                .executes { ranksCommand(it) }
        )
    }

    fun ranksCommand(context: Context): Int {
        val ranksInfo = hover(
            click(
                darkGreen("The server relies on donations to survive.\nTo thank our donors, we give them some neat perks\n"),
                ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/donate"
                )
            ),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                green("Click here to donate")
            )
        )
        val rank1 = green("$") + darkGray("5 ") +
                gray("[") + darkGreen("VIP") + gray("] ") +
                lightPurple("5 homes, more perks")
        val rank2 = green("$") + darkGray("10 ") +
                gray("[") + darkGreen("VIP") + green("+") + gray("] ") +
                lightPurple("8 homes, more perks")
        val rank3 = green("$") + darkGray("15 ") +
                gray("[") + gold("MVP") + gray("] ") +
                lightPurple("12 homes, more perks")
        val rank4 = green("$") + darkGray("20 ") +
                gray("[") + gold("MVP") + green("+") + gray("] ") +
                lightPurple("20 homes, more perks")

        context.source.player.sendSystemMessage(
            ranksInfo,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rank1,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rank2,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rank3,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rank4,
            Util.NIL_UUID
        )

        return 1
    }
}