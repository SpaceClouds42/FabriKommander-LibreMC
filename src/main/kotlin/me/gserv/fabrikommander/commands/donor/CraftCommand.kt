package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.Node
import net.minecraft.screen.AnvilScreenHandler
import net.minecraft.screen.CraftingScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.command.CommandManager
import net.minecraft.stat.Stats
import net.minecraft.text.LiteralText

class CraftCommand(val dispatcher: Dispatcher) {
    fun register() {
        val hatNode: Node =
            CommandManager
                .literal("craft")
                .requires { it.player.hasRankPermissionLevel("VIP") }
                .executes { craftCommand(it) }
                .build()

        dispatcher.root.addChild(hatNode)
    }

    private fun craftCommand(context: Context): Int {
        val player = context.source.player
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory(
                { syncId, inv, _ -> CraftingScreenHandler(syncId, inv, ScreenHandlerContext.EMPTY) },
                LiteralText("Crafting Table")
            )
        )
        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        return 1
    }
}