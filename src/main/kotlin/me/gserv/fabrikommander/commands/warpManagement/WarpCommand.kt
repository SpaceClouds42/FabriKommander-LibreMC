package me.gserv.fabrikommander.commands.warpManagement

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.WarpDataManager
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

class WarpCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("warp")
                .then(
                    CommandManager.argument("name", StringArgumentType.word())
                        .executes { warpCommand(it, StringArgumentType.getString(it, "name")) }
                        .suggests { context, builder ->
                            WarpDataManager.getWarps()?.forEach {
                                builder.suggest(it.name)
                            }

                            builder.buildFuture()
                        }
                )
        )
    }

    fun warpCommand(context: Context, name: String): Int {
        val player = context.source.player
        val warp = WarpDataManager.getWarp(name)

        if (warp == null) {
            context.source.sendFeedback(
                red("Unknown warp: ") + aqua(name),
                false
            )
        } else {
            val world = player.server.getWorld(RegistryKey.of(Registry.DIMENSION, warp.pos.world))

            if (world == null) {
                context.source.sendFeedback(
                    red("Warp ") +
                            aqua(name) +
                            red(" is in a world ") +
                            yellow("(") +
                            aqua(identifierToWorldName(warp.pos.world)) +
                            yellow(") ") +
                            red("that no longer exists."),
                    false
                )
            } else {
                PlayerDataManager.setBackPos(
                    player.uuid,
                    Pos(
                        x = player.x,
                        y = player.y,
                        z = player.z,
                        world = player.world.registryKey.value,
                        yaw = player.yaw,
                        pitch = player.pitch
                    )
                )

                player.teleport(world, warp.pos.x, warp.pos.y, warp.pos.z, warp.pos.yaw, warp.pos.pitch)

                context.source.sendFeedback(
                    green("Teleported to warp: ") + aqua(name),
                    true
                )
            }
        }

        return 1
    }
}