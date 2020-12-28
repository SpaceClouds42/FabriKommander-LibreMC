package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.TeleportRequest
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.MutableText

class TpCancelCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            literal("tpcancel").then(
                argument("target", EntityArgumentType.player()).executes(this::tpCancelCommand)
            )
        )
    }

    fun tpCancelCommand(context: Context): Int {
        val target = EntityArgumentType.getPlayer(context, "target")
        val messageHeader = gray("[") + yellow("TPA") + gray("] ") + reset("")
        if (TeleportRequest.ACTIVE_REQUESTS[context.source.player.uuidAsString + target.uuidAsString] == null) {
            context.source.sendError(
                messageHeader + 
                red("No active teleport request to ") + target.displayName
            )
            return 0
        }
        TeleportRequest.ACTIVE_REQUESTS[context.source.player.uuidAsString + target.uuidAsString]!!.notifyTargetOfCancel()
        TeleportRequest.ACTIVE_REQUESTS.remove(context.source.player.uuidAsString + target.uuidAsString)
        context.source.sendFeedback(
            messageHeader +     
            red("Teleport request to ") + reset("") +
            target.displayName + reset("") +
            red(" was cancelled."),
            true
        )
        return 1
    }
}
