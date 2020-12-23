package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.util.Util

class InfoCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("info")
                .executes { infoCommand(it) }
        )
    }

    fun infoCommand(context: Context): Int {
        val info1 = darkGreen("Worldborder: 1 mil, expanding to 15 mil upon 1.17 release")
        val info2 = hover(
            darkAqua("Danger Zone: /rtp has a 5000 block radius"),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                red("Be sure to build your base past 5000 blocks")
            )
        )
        val info3 = hover(
            click(
                darkGreen("Home Limit: Everyone gets a home limit of 3 homes, donors unlock more"),
                ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/ranks"
                )
            ),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                yellow("Click here to read more about ranks")
            )
        )
        val info4 = darkAqua("Discord: Get the invite to the Discord server with /discord")

        context.source.player.sendSystemMessage(
            info1,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            info2,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            info3,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            info4,
            Util.NIL_UUID
        )

        return 1
    }
}