package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent

class VoteCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("vote")
                .executes { discordCommand(it) }
        )
    }

    fun discordCommand(context: Context): Int {
        context.source.sendFeedback(
            green(underline("Vote for the server\n")) +
            hover(
                click(
                    aqua("Minecraft-MP\n"),
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://minecraft-mp.com/server/273982/vote/"
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    aqua("Click here to vote on Minecraft-MP")
                )
            ) +
            hover(
                click(
                    aqua("TopG\n"),
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://topg.org/Minecraft/in-619794"
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    aqua("Click here to vote on TopG")
                )
            ) +
            hover(
                click(
                    aqua("Planet Minecraft\n"),
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://www.planetminecraft.com/server/libremc/vote/"
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    aqua("Click here to vote on Planet Minecraft")
                )
            ) +
            hover(
                click(
                    aqua("Minecraft Servers\n"),
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://minecraftservers.org/vote/601599"
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    aqua("Click here to vote on Minecraft Servers")
                )
            ) +
            hover(
                click(
                    aqua("Minecraft Server List"),
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://minecraft-server-list.com/server/471304/vote/"
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    aqua("Click here to vote on Minecraft Server List")
                )
            ),
            false
        )

        return 1
    }
}