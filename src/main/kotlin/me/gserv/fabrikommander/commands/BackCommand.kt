package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.aqua
import me.gserv.fabrikommander.utils.gold
import me.gserv.fabrikommander.utils.green
import me.gserv.fabrikommander.utils.gray
import me.gserv.fabrikommander.utils.yellow
import me.gserv.fabrikommander.utils.reset
import me.gserv.fabrikommander.utils.identifierToWorldName
import me.gserv.fabrikommander.utils.plus
import me.gserv.fabrikommander.utils.red
import me.gserv.fabrikommander.utils.yellow
import net.minecraft.util.Util
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

class BackCommand(val dispatcher: Dispatcher) {
    companion object Utils {
        // Mixin had a problem with @Shadow-ing sendMessage so I'm doing it here
        // Also, apparently functions inside companion objects are automatically static, neat!
        fun sendDeathMessage(p: PlayerEntity) {
            p.sendMessage(
                gray("[") + yellow("Back") + gray("] ") + reset("") + 
                gold("Use /back to get back to your death location."),
                false
            )
        }
    }

    fun register() {
        dispatcher.register(
            literal("back").executes(::backCommand)
        )
    }

    fun backCommand(context: Context): Int {
        val player = context.source.player
        val backHeader = gray("[") + yellow("Back") + gray("] ") + reset("")
        if (PlayerDataManager.getBackPos(player.uuid) == null) {
            context.source.sendError(
                backHeader + red("No last position defined - you will have to teleport.")
            )
            return 0
        }
        val pos: Pos = PlayerDataManager.getBackPos(player.uuid)!! // pos is not null, we can be sure of that
        val world = player.server.getWorld(RegistryKey.of(Registry.DIMENSION, pos.world))
        if (world == null) {
            context.source.sendError(
                backHeader + 
                red("Last position is in a world ") +
                        yellow("(") +
                        aqua(identifierToWorldName(pos.world)) +
                        yellow(") ") +
                        red("that no longer exists.")
            )
            return 0
        }
        PlayerDataManager.setBackPos(
            player.uuid, Pos( // Thanks to @SpaceClouds42 for suggesting this
                world = player.world.registryKey.value,

                x = player.x,
                y = player.y,
                z = player.z,

                yaw = player.yaw,
                pitch = player.pitch
            )
        )
        player.teleport(world, pos.x, pos.y, pos.z, pos.yaw, pos.pitch)
        context.source.sendFeedback(
            backHeader + gold("Successfully teleported back to ") +
            aqua("[${pos.x.toInt()}, ${pos.y.toInt()}, ${pos.z.toInt()}]"),
            false
        )
        return 1
    }
}
