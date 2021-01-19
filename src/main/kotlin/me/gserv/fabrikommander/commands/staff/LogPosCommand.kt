package me.gserv.fabrikommander.commands.staff

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

class LogPosCommand(val dispatcher: Dispatcher) {
    fun register() {
        val logPosNode: Node =
            CommandManager
                .literal("logpos")
                .requires { it.player.hasRankPermissionLevel("Helper") }
                .then(
                    CommandManager
                        .argument("player", GameProfileArgumentType.gameProfile())
                        .executes { logPosCommand(
                            it,
                            requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next())
                        ) }
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)
                            builder.buildFuture()
                        }
                )
                .build()
        dispatcher.root.addChild(logPosNode)
    }

    fun logPosCommand(context: Context, targetPlayer: ServerPlayerEntity): Int {
        val player = context.source.player
        val lastLogPos = PlayerDataManager.getLogPos(targetPlayer.uuid)

        if (lastLogPos == null) {
            context.source.sendError(
                aqua(targetPlayer.entityName) +
                red(" has not logged out since the addition of the logpos command")
            )
            return 0
        }

        context.source.sendFeedback(
            green("Teleported to the logout position of ") +
                    aqua(targetPlayer.entityName),
            true
        )
        PlayerDataManager.setBackPos(player.uuid, Pos(player.x, player.y, player.z, player.yaw, player.pitch, player.world.registryKey.value))
        player.teleport(
            player.server.getWorld(RegistryKey.of(Registry.DIMENSION, lastLogPos.world)),
            lastLogPos.x,
            lastLogPos.y,
            lastLogPos.z,
            lastLogPos.yaw,
            lastLogPos.pitch
        )
        return 1
    }
}