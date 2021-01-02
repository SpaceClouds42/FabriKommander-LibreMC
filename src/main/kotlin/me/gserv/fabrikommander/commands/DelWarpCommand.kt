package me.gserv.fabrikommander.commands

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.WarpDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager

class DelWarpCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("delwarp")
                .then(
                    CommandManager.argument("name", StringArgumentType.word())
                        .requires { it.hasPermissionLevel(2) }
                        .executes {
                            delWarpCommand(
                                it,
                                StringArgumentType.getString(it, "name")
                            )
                        }
                        .suggests { context, builder ->
                            PlayerDataManager.getHomes(context.source.player.uuid)?.forEach {
                                builder.suggest(it.name)
                            }

                            builder.buildFuture()
                        }
                )
        )
    }

    fun delWarpCommand(context: Context, name: String): Int {
        val warp = WarpDataManager.getWarp(name)

        if (warp == null) {
            context.source.sendFeedback(
                red("Unknown warp: ") + aqua(name),
                false
            )
        } else {
            WarpDataManager.deleteWarp(name)

            context.source.sendFeedback(
                green("Warp deleted: ") + aqua(name),
                true
            )
        }

        return 1
    }
}