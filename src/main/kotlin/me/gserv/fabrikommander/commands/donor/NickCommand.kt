package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.util.Util

class NickCommand(val dispatcher: Dispatcher) {
    fun register() {
        val nickNode: Node =
            CommandManager
                .literal("nick")
                .build()
        val getNode: Node =
            CommandManager
                .literal("get")
                .executes { nickGetCommand(
                    it,
                    it.source.player
                ) }
                .then(
                    CommandManager
                        .argument("player", GameProfileArgumentType.gameProfile())
                        .executes { nickGetCommand(
                            it,
                            requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next())
                        ) }
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                )
                .build()
        val setNode: Node =
            CommandManager
                .literal("set")
                .requires { hasRankPermissionLevel(it.player, "VIP") }
                .then(
                    CommandManager
                        .argument("newNick", StringArgumentType.greedyString())
                        .executes { nickSetCommand(
                            it,
                            it.source.player,
                            StringArgumentType.getString(it, "newNick")
                        ) }
                )
                .build()
        val forceSetNode: Node =
            CommandManager
                .literal("forceSet")
                .requires { hasRankPermissionLevel(it.player, "Helper") }
                .then(
                    CommandManager
                        .argument("player", GameProfileArgumentType.gameProfile())
                        .then(
                            CommandManager
                                .argument("newNick", StringArgumentType.greedyString())
                                .executes { nickSetCommand(
                                    it,
                                    requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next()),
                                    StringArgumentType.getString(it, "newNick")
                                ) }
                        )
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                )
                .build()
        val removeNode: Node =
            CommandManager
                .literal("remove")
                .requires { hasRankPermissionLevel(it.player, "VIP") }
                .executes { nickRemoveCommand(
                    it,
                    it.source.player
                ) }
                .then(
                    CommandManager
                        .argument("player", GameProfileArgumentType.gameProfile())
                        .requires { hasRankPermissionLevel(it.player, "Helper") }
                        .executes { nickRemoveCommand(
                            it,
                            requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next())
                        ) }
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                )
                .build()
        dispatcher.root.addChild(nickNode)
        nickNode.addChild(getNode)
        nickNode.addChild(setNode)
        nickNode.addChild(removeNode)
        nickNode.addChild(forceSetNode)
    }

    fun nickGetCommand(context: Context, targetPlayer: ServerPlayerEntity): Int {
        val nick = PlayerDataManager.getNick(targetPlayer.uuid)
        if (context.source.player != targetPlayer && nick == null) {
            context.source.sendFeedback(
                        aqua(targetPlayer.entityName) +
                        green(" does not have a nick"),
                true
            )
        } else if (context.source.player == targetPlayer && nick == null) {
            context.source.sendFeedback(
                        green("You do not have a nick"),
                false
            )
        } else if (context.source.player != targetPlayer) {
            context.source.sendFeedback(
                reset("") +
                        aqua(targetPlayer.entityName) +
                        green(" has the nick: ") +
                        reset(nick!!),
                true
            )
        } else {
            context.source.sendFeedback(
                reset("") +
                        green("You have the nick: ") +
                        reset(nick!!),
                false
            )
        }

        return 1
    }

    fun nickRemoveCommand(context: Context, targetPlayer: ServerPlayerEntity): Int {
        PlayerDataManager.setNick(targetPlayer.uuid, null)
        if (context.source.player != targetPlayer) {
            context.source.sendFeedback(
                red("Removed nick of ") +
                        aqua(targetPlayer.entityName),
                true
            )
            targetPlayer.sendSystemMessage(
                red("Your nick has been removed by ") +
                        aqua(context.source.player.entityName),
                Util.NIL_UUID
            )
        } else {
            context.source.sendFeedback(
                        red("Your nick has been removed"),
                false
            )
        }

        return 1
    }

    fun nickSetCommand(context: Context, targetPlayer: ServerPlayerEntity, newNick: String): Int {
        val nick = minecraftFormatting(newNick)
        PlayerDataManager.setNick(targetPlayer.uuid, nick)
        if (context.source.player != targetPlayer) {
            context.source.sendFeedback(
                reset("") +
                        green("Set nick of player ") +
                        aqua(targetPlayer.entityName) +
                        green(" to ") +
                        reset(newNick),
                true
            )
        } else {
            context.source.sendFeedback(
                reset("") +
                        green("Your nick is now ") +
                        reset(newNick),
                false
            )
        }

        return 1
    }
}