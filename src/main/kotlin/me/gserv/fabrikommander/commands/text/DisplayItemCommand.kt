package me.gserv.fabrikommander.commands.text

import me.gserv.fabrikommander.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Util
import java.util.function.Consumer

class DisplayItemCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("displayitem")
                .executes { displayItemCommand(it) }
        )
    }

    fun displayItemCommand(context: Context): Int {
        val player = context.source.player
        val itemStack = player.getStackInHand(Hand.MAIN_HAND)

        if (itemStack == ItemStack.EMPTY) {
            context.source.sendFeedback(
                red("You're currently not holding anything!"),
                false)
        } else {
            context.source.minecraftServer.playerManager.playerList.forEach(
                Consumer { p: ServerPlayerEntity ->
                    p.sendSystemMessage(
                        reset("") + player.displayName + reset("") +
                                yellow(" wants to show you their ") +
                                itemStack.toHoverableText(),
                        Util.NIL_UUID
                    )
                }
            )
        }

        return 1
    }
}