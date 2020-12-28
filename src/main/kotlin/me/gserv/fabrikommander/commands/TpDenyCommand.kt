package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.TeleportRequest
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal

class TpDenyCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            literal("tpdeny").then(
                argument("source", EntityArgumentType.player()).executes(this::tpDenyCommand)
            )
        )
    }

    fun tpDenyCommand(context: Context): Int {
        val source = EntityArgumentType.getPlayer(context, "source")
        val messageHeader = gray("[") + yellow("TPA") + gray("] ") + reset("")
        if (TeleportRequest.ACTIVE_REQUESTS[source.uuidAsString + context.source.player.uuidAsString] == null) {
            context.source.sendError(
                messageHeader + red("No active teleport request from ") + source.displayName
            )
            return 0
        }
        TeleportRequest.ACTIVE_REQUESTS[source.uuidAsString + context.source.player.uuidAsString]!!.notifySourceOfDeny()
        TeleportRequest.ACTIVE_REQUESTS.remove(source.uuidAsString + context.source.player.uuidAsString)
        context.source.sendFeedback(
            messageHeader + aqua("Teleport request from ") +
                    source.displayName + reset("") +
                    aqua(" was ") + red("denied"),
            true
        )
        return 1
    }
}
