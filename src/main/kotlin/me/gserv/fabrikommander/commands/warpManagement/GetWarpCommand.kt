package me.gserv.fabrikommander.commands.warpManagement

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.WarpDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager

class GetWarpCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("getwarp")
                .then(
                    CommandManager.argument("name", StringArgumentType.word())
                        .executes { getWarp(it, StringArgumentType.getString(it, "name")) }
                        .suggests { context, builder ->
                            WarpDataManager.getWarps()?.forEach {
                                builder.suggest(it.name)
                            }

                            builder.buildFuture()
                        }
                )
        )
    }

    fun getWarp(context: Context, name: String): Int {
        val warp = WarpDataManager.getWarp(name)

        if (warp == null) {
            context.source.sendFeedback(
                red("Unknown warp: ") + aqua(name),
                false
            )
        } else {
            context.source.sendFeedback(
                yellow("Warp ") + aqua(name) + yellow(": [") +
                        green("World: ") + aqua(warp.pos.world.toString()) + yellow(", ") +
                        green("X: ") + aqua(warp.pos.x.toInt().toString()) + yellow(", ") +
                        green("Y: ") + aqua(warp.pos.y.toInt().toString()) + yellow(", ") +
                        green("Z: ") + aqua(warp.pos.z.toInt().toString()) + yellow(", ") +
                        green("Pitch: ") + aqua(warp.pos.pitch.toInt().toString()) + yellow(", ") +
                        green("Yaw: ") + aqua(warp.pos.yaw.toInt().toString()) + yellow("] ") +
                        green("Created by: ${warp.createdBy}"),
                false
            )
        }

        return 1
    }
}
