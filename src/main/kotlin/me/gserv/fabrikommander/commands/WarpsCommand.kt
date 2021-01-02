package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.WarpDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

class WarpsCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("warps")
                .executes { warpsCommand(it) }
        )
    }

    fun warpsCommand(context: Context): Int {
        val warps = WarpDataManager.getWarps()

        if (warps == null || warps.isEmpty()) {
            context.source.sendFeedback(
                red("No warps found."),
                false
            )
        } else {
            var text = green("Warps: ")

            for (element in warps.withIndex()) {
                val world = context.source.minecraftServer.getWorld(RegistryKey.of(Registry.DIMENSION, element.value.pos.world))

                if (world == null) {
                    text += hover(
                        red(element.value.name),

                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            red("This warp is in a world ") +
                                    yellow("(") +
                                    aqua(identifierToWorldName(element.value.pos.world)) +
                                    yellow(") ") +
                                    red("that no longer exists.")
                        )
                    )
                } else {
                    text += click(
                        hover(
                            darkAqua(element.value.name),
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                white("This warp is in: ") +
                                        darkAqua(identifierToWorldName(element.value.pos.world)) +
                                        white(".\n") +
                                        yellow("Click to teleport!") +
                                        gray("Created by: ${element.value.createdBy}")
                            )
                        ),
                        ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp ${element.value.name}")
                    )
                }

                if (element.index < warps.size - 1) text += yellow(", ")
            }

            context.source.sendFeedback(text, false)
        }

        return 1
    }
}