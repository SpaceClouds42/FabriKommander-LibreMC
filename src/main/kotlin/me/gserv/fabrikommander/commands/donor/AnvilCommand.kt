package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.Node
import net.minecraft.screen.AnvilScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.command.CommandManager
import net.minecraft.stat.Stats
import net.minecraft.text.LiteralText

class AnvilCommand(val dispatcher: Dispatcher) {
    fun register() {
        val hatNode: Node =
            CommandManager
                .literal("anvil")
                .requires { it.player.hasRankPermissionLevel("VIP") }
                .executes { anvilCommand(it) }
                .build()

        dispatcher.root.addChild(hatNode)
    }

    private fun anvilCommand(context: Context): Int {
        val player = context.source.player
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory(
                { syncId, inv, _ -> AnvilScreenHandler(syncId, inv, ScreenHandlerContext.EMPTY) },
                LiteralText("Anvil")
            )
        )
        player.incrementStat(Stats.INTERACT_WITH_ANVIL)
        return 1
    }
}