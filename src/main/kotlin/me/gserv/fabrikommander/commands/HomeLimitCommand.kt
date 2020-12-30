package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.text.MutableText

class HomeLimitCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("homelimit")
                .executes { homeLimitCommand(it, it.source.player) }
                .then(
                    CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                        .requires { it.hasPermissionLevel(2) }
                        .executes { homeLimitCommand(
                            it,
                            requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next())
                        ) }
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                        .then(
                            CommandManager.argument("newLimit", IntegerArgumentType.integer(3, 100))
                                .executes { homeLimitCommand(
                                    it,
                                    requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next()),
                                    IntegerArgumentType.getInteger(it, "newLimit")
                                ) }
                        )
                )
        )
    }

    fun homeLimitCommand(context: Context, targetPlayer: ServerPlayerEntity, newHomeLimit: Int): Int {
        PlayerDataManager.setHomeLimit(targetPlayer.uuid, newHomeLimit)

        context.source.sendFeedback(
            green("Set home limit of player ") +
                    targetPlayer.displayName +
                    green(" to ") +
                    gold("$newHomeLimit"),
            true
        )

        return 1
    }

    fun homeLimitCommand(context: Context, targetPlayer: ServerPlayerEntity): Int {
        var homeLimit = PlayerDataManager.getHomeLimit(targetPlayer.uuid)
        if (homeLimit == null) {
            homeLimit = 3
        }
        if (context.source.player != targetPlayer) {
            context.source.sendFeedback(
                targetPlayer.displayName as MutableText + reset("") +
                        green(" has a limit of ") +
                        gold("$homeLimit") +
                        green(" homes, and is using ") +
                        gold("${PlayerDataManager.getHomes(targetPlayer.uuid)!!.size}") +
                        green(
                            when (PlayerDataManager.getHomes(targetPlayer.uuid)!!.size == 1) {
                                true -> " home"
                                false -> " homes"
                            }
                        ),
                false
            )
        } else {
            context.source.sendFeedback(
                        green(" You have a limit of ") +
                        gold("$homeLimit") +
                        green(" homes, and you have ") +
                        gold("${PlayerDataManager.getHomes(targetPlayer.uuid)!!.size}") +
                        green(
                            when (PlayerDataManager.getHomes(targetPlayer.uuid)!!.size == 1) {
                                true -> " home"
                                false -> " homes"
                            }
                        ),
                false
            )
        }

        return 1
    }
}