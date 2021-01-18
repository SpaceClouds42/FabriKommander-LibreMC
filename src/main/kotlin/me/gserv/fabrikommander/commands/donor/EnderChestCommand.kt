package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText

class EnderChestCommand(val dispatcher: Dispatcher) {
    fun register() {
        val enderChestNode: Node =
            CommandManager
                .literal("echest")
                .requires { it.player.hasRankPermissionLevel("VIP") }
                .executes { enderChestCommand(it) }
                .then(
                    CommandManager
                        .argument("player", GameProfileArgumentType.gameProfile())
                        .requires { it.player.hasRankPermissionLevel("Helper") }
                        .executes { enderChestCommand(it, requestPlayer(it, GameProfileArgumentType.getProfileArgument(it, "player").iterator().next())) }
                        .suggests { context, builder ->
                            context.source.minecraftServer.playerNames.forEach(builder::suggest)

                            builder.buildFuture()
                        }
                )
                .build()

        dispatcher.root.addChild(enderChestNode)
    }

    private fun enderChestCommand(context: Context): Int {
        return enderChestCommand(context, context.source.player)
    }

    private fun enderChestCommand(context: Context, targetPlayer: ServerPlayerEntity): Int {
        val player = context.source.player
        val enderChestInventory: EnderChestInventory = targetPlayer.enderChestInventory

        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory(
                { syncId, playerInventory, _ -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, enderChestInventory) },
                TranslatableText("container.enderchest")
            )
        )
        player.incrementStat(Stats.OPEN_ENDERCHEST)

        return 1
    }
}