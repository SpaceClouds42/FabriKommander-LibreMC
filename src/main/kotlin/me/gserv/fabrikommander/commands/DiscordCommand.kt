package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent

class DiscordCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("discord")
                .executes { discordCommand(it) }
        )
    }

    fun discordCommand(context: Context): Int {
        context.source.sendFeedback(
            hover(
                click(
                    blue("Join the ") +
                            bold(
                                aqua("LibreMC")
                            ) +
                            blue(
                                reset(" Discord server!")
                            ),
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://discord.gg/KDq7bbSfgn"
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    blue("Click here to join")
                )
            ),
            false
        )

        return 1
    }
}
