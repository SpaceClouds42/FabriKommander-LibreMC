package me.gserv.fabrikommander.commands

import com.mojang.brigadier.arguments.ArgumentType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.NumberRangeArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import com.mojang.brigadier.arguments.IntegerArgumentType

class HomeLimitCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("homelimit")
                .requires { it.hasPermissionLevel(2) }
                .then(
                    CommandManager.argument("player", EntityArgumentType.player())
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                )
                    .then(
                        CommandManager.argument("newLimit", IntegerArgumentType.integer(3, 100))
                            .executes { homeLimitCommand(it,
                                EntityArgumentType.getPlayer(it, "player"),
                                IntegerArgumentType.getInteger(it, "newLimit")) }
                    )
        )
    }

    fun homeLimitCommand(context: Context, targetPlayer: ServerPlayerEntity, newHomeLimit: Int): Int {
        val player = context.source.player

        PlayerDataManager.setHomeLimit(player.uuid, newHomeLimit)

        context.source.sendFeedback(
            green("Set home limit of player ") +
                    player.displayName +
                    green(" to ") +
                    gold("$newHomeLimit"),
            true
        )

        return 1
    }
}