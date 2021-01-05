package me.gserv.fabrikommander.commands.TPA

import me.gserv.fabrikommander.data.TeleportRequest
import me.gserv.fabrikommander.utils.*
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal

// I'm not sure how I should capitalise this
class TpAcceptCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            literal("tpaccept").then(
                argument("source", EntityArgumentType.player()).executes(this::tpAcceptCommand)
            )
        )
    }

    fun tpAcceptCommand(context: Context): Int {
        val source = EntityArgumentType.getPlayer(context, "source")
        val request = TeleportRequest.ACTIVE_REQUESTS[source.uuidAsString + context.source.player.uuidAsString]
        val messageHeader = gray("[") + yellow("TPA") + gray("] ") + reset("")
        if (request == null) {
            context.source.sendError(
                messageHeader + 
                red("No active teleport request from ") + source.displayName
            )
            return 0
        }
        request.apply()
        request.notifySourceOfAccept()
        TeleportRequest.ACTIVE_REQUESTS.remove(source.uuidAsString + context.source.player.uuidAsString)
        context.source.sendFeedback(
            messageHeader + aqua("Teleport request from ") +
                    source.displayName + reset("") +
                    aqua(" was ") + green("accepted"),
            true
        )
        return 1
    }
}
