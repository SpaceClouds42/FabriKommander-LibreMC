package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.Node
import net.minecraft.block.AbstractBlock
import net.minecraft.block.AnvilBlock
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.AnvilScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.command.CommandManager
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.stat.Stats
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos

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
        /*val handledScreen =
            SimpleNamedScreenHandlerFactory({ syncId: Int, playerInventory: PlayerInventory?, _: PlayerEntity? ->
                AnvilScreenHandler(
                    syncId,
                    playerInventory,
                    ScreenHandlerContext.create(player.world, BlockPos(player.x, player.y, player.z))
                )
            }, TranslatableText("container.repair"))
        player.openHandledScreen(handledScreen)*/
        /*val state = AnvilBlock(
            AbstractBlock.Settings.of(Material.REPAIR_STATION, MapColor.IRON)
                .requiresTool()
                .strength(5.0f, 1200.0f)
                .sounds(BlockSoundGroup.ANVIL)
        ).defaultState

        player.openHandledScreen(state.createScreenHandlerFactory(player.world, BlockPos(player.x, player.y, player.z)))*/
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory(
                { syncId, inv, _ -> AnvilScreenHandler(syncId, inv, ScreenHandlerContext.EMPTY) },
                TranslatableText("container.repair")
            )
        )
        player.incrementStat(Stats.INTERACT_WITH_ANVIL)
        return 1
    }
}