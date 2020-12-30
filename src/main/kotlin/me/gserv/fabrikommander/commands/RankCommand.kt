package me.gserv.fabrikommander.commands

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity

class RankCommand(val dispatcher: Dispatcher) {
    val rankHomeLimitMap = hashMapOf(
        "member" to 3,
        "MVP" to 5,
        "MVP+" to 8,
        "VIP" to 12,
        "VIP+" to 20,
        "Helper" to 8,
        "Mod" to 8
    )

    fun register() {
        dispatcher.register(
            CommandManager.literal("rank")
                .executes { rankCommand(it, it.source.player) }
                .then(
                    CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                        .executes { rankCommand(
                            it,
                            requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next())
                        ) }
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                        .then(
                            CommandManager.argument("newRank", StringArgumentType.string())
                                .requires {
                                    it.hasPermissionLevel(2) ||
                                    rankPermission[PlayerDataManager.getRank(it.player.uuid)]!! >= 5
                                }
                                .executes { rankCommand(
                                    it,
                                    requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next()),
                                    StringArgumentType.getString(it, "newRank")
                                ) }
                                .suggests { context, builder ->
                                    rankPermission.forEach {
                                        builder.suggest(it.key)
                                    }

                                    builder.buildFuture()
                                }
                        )
                )
        )
    }

    // Staff command for setting ranks
    fun rankCommand(context: Context, targetPlayer: ServerPlayerEntity, newRank: String): Int {
        if (rankPermission[newRank] == null) {
            context.source.sendError(
                red("Rank '$newRank' does not exist")
            )

            return 1
        } else if (rankPermission[newRank]!! >= rankPermission[PlayerDataManager.getRank(context.source.player.uuid)]!!) {
            context.source.sendError(
                red("Your rank is not high enough to give the '$newRank' rank")
            )

            return 1
        }

        PlayerDataManager.setRank(targetPlayer.uuid, newRank)
        context.source.sendFeedback(
            green("Gave ") +
                    aqua(targetPlayer.entityName) +
                    green(" the ") +
                    aqua(newRank) +
                    green(" rank"),
            false
        )

        HomeLimitCommand(dispatcher).homeLimitCommand(
            context,
            targetPlayer,
            rankHomeLimitMap[newRank]!!
        )

        return 1
    }

    // Anybody command for getting ranks
    fun rankCommand(context: Context, targetPlayer: ServerPlayerEntity): Int {
        val rank = PlayerDataManager.getRank(targetPlayer.uuid)

        if (context.source.player != targetPlayer) {
            context.source.sendFeedback(
                aqua(targetPlayer.entityName) +
                        green(" has the ") +
                        aqua("$rank") +
                        green(" rank"),
                false
            )
        } else {
            context.source.sendFeedback(
                        green("You have the ") +
                        aqua("$rank") +
                        green(" rank") + if (!rank.equals("member")) {green(", thanks for donating")} else {green("")},
                false
            )
        }

        return 1
    }
}