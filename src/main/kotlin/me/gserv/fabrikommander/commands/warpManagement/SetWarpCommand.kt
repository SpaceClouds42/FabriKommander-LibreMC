package me.gserv.fabrikommander.commands.warpManagement

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.WarpDataManager
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.data.spec.Warp
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager

class SetWarpCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("setwarp")
                .then(
                    CommandManager.argument("name", StringArgumentType.word())
                        .requires { it.hasPermissionLevel(2) }
                        .executes { setWarpCommand(it, StringArgumentType.getString(it, "name")) }
                )
        )
    }

    fun setWarpCommand(context: Context, name: String): Int {
        val player = context.source.player

        val warp = Warp(
            name = name,

            pos = Pos(
                x = player.x,
                y = player.y,
                z = player.z,
                yaw = player.yaw,
                pitch = player.pitch,
                world = player.world.registryKey.value
            ),

            createdBy = player.entityName
        )

        WarpDataManager.setWarp(warp)

        context.source.sendFeedback(
            green("Warp created: ") + aqua(name),
            true
        )

        return 1
    }
}