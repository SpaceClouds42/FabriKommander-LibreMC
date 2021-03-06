package me.gserv.fabrikommander.commands.staff

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util

class RankCommand(val dispatcher: Dispatcher) {
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
                            CommandManager.argument("rank", StringArgumentType.string())
                                .requires {
                                    it.hasPermissionLevel(2) ||
                                    hasRankPermissionLevel(it.player, "Helper")
                                }
                                .executes { rankCommand(
                                    it,
                                    requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next()),
                                    StringArgumentType.getString(it, "rank")
                                ) }
                                .suggests { context, builder ->
                                    ranks.forEach {
                                        builder.suggest(it)
                                    }

                                    builder.buildFuture()
                                }
                        )
                )
        )
    }

    // Staff command for setting ranks
    fun rankCommand(context: Context, targetPlayer: ServerPlayerEntity, newRank: String): Int {
        if (!ranks.contains(newRank)) {
            context.source.sendError(
                red("Rank '$newRank' does not exist")
            )

            return 1
        } else if (
            !context.source.player.hasPermissionLevel(2) && // Not /opped
            newRank in staffRanks // new rank is staff rank
        ) {
            context.source.sendError(
                red("You do not have permission to give the '$newRank' rank")
            )

            return 1
        }

        changeHomeLimit(context, targetPlayer, newRank)
        kickFromStaffChat(targetPlayer, newRank)
        PlayerDataManager.setRank(targetPlayer.uuid, newRank)
        context.source.sendFeedback(
            green("Gave ") +
                    aqua(targetPlayer.entityName) +
                    green(" the ") +
                    aqua(newRank) +
                    green(" rank"),
            true
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

    private fun changeHomeLimit(context: Context, targetPlayer: ServerPlayerEntity, newRank: String) {
        val isNewRankMoreHomes = rankToHomeLimit[newRank]!! > PlayerDataManager.getHomeLimit(targetPlayer.uuid)!!
        val isNewRankHigher = ranks.indexOf(newRank) > ranks.indexOf(PlayerDataManager.getRank(targetPlayer.uuid))
        if (targetPlayer.hasPermissionLevel(2)) {
            PlayerDataManager.setHomeLimit(targetPlayer.uuid, 0)
            context.source.sendFeedback(
                green("Set home limit of player ") +
                        targetPlayer.displayName +
                        green(" to ") +
                        gold("infinite"),
                false
            )
        } else if (isNewRankHigher && isNewRankMoreHomes) {
            PlayerDataManager.setHomeLimit(targetPlayer.uuid, rankToHomeLimit[newRank]!!)
            context.source.sendFeedback(
                green("Set home limit of player ") +
                        targetPlayer.displayName +
                        green(" to ") +
                        gold("${rankToHomeLimit[newRank]}"),
                false
            )
        } else {
            context.source.sendFeedback(
                green("Did not change home limit of player ") +
                        targetPlayer.displayName,
                false
            )
        }
    }

    private fun kickFromStaffChat(targetPlayer: ServerPlayerEntity, newRank: String) {
        if (newRank !in staffRanks && PlayerDataManager.isInStaffChat(targetPlayer.uuid)!!) {
            PlayerDataManager.setInStaffChat(targetPlayer.uuid, false)
            targetPlayer.sendSystemMessage(
                gray("[") + yellow("Staff") + gray("] ") + red("Left Staff Chat"),
                Util.NIL_UUID
            )
        }
    }
}