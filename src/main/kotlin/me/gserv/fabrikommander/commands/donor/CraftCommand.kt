package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.Node
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.*
import net.minecraft.server.command.CommandManager
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos

class CraftCommand(val dispatcher: Dispatcher) {
    fun register() {
        val craftNode: Node =
            CommandManager
                .literal("craft")
                .requires { it.player.hasRankPermissionLevel("VIP") }
                .executes { craftCommand(it) }
                .build()

        dispatcher.root.addChild(craftNode)
    }

    private fun craftCommand(context: Context): Int {
        val player = context.source.player

        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory(
                { syncId, inv, _ -> CraftingScreenHandler(syncId, inv, ScreenHandlerContext.EMPTY) },
                TranslatableText("container.crafting")
            )
        )
        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        return 1
    }
}