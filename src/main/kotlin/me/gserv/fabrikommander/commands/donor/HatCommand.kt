package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.util.Hand

class HatCommand(val dispatcher: Dispatcher) {
    fun register() {
        val hatNode: Node =
            CommandManager
                .literal("hat")
                .requires { it.player.hasRankPermissionLevel("VIP") }
                .executes { hatCommand(it) }
                .build()

        dispatcher.root.addChild(hatNode)
    }

    private fun hatCommand(context: Context): Int {
        val player = context.source.player
        val heldItemStack = player.mainHandStack
        val helmetItemStack = player.inventory.armor[3]

        if (heldItemStack.isEmpty) {
            context.source.sendError(
                red("Must be holding an item to wear it as a hat")
            )
            return 0
        }
        if (heldItemStack.count != 1) {
            context.source.sendError(
                red("You can only wear one item as a hat at a time")
            )
            return 0
        }

        player.setStackInHand(Hand.MAIN_HAND, helmetItemStack)
        player.inventory.armor[3] = heldItemStack
        context.source.sendFeedback(
            green("You are now wearing ") +
                    aqua(heldItemStack.name.copy()) +
                    green(" as a hat"),
            false
        )
        return 1
    }
}