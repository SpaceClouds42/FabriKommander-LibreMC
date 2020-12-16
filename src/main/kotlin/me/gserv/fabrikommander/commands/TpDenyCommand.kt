package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.TeleportRequest
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.plus
import me.gserv.fabrikommander.utils.aqua
import me.gserv.fabrikommander.utils.darkPurple
import me.gserv.fabrikommander.utils.red
import me.gserv.fabrikommander.utils.gray
import me.gserv.fabrikommander.utils.yellow
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal

class TpDenyCommand(val dispatcher: Dispatcher) {
    val messageHeader = gray("[") + yellow("TPA") + gray("] ") + reset("")

    fun register() {
        dispatcher.register(
            literal("tpdeny").then(
                argument("source", EntityArgumentType.player()).executes(this::tpDenyCommand)
            )
        )
    }

    fun tpDenyCommand(context: Context): Int {
        val source = EntityArgumentType.getPlayer(context, "source")
        if (TeleportRequest.ACTIVE_REQUESTS[source.uuidAsString + context.source.player.uuidAsString] == null) {
            context.source.sendError(
                messageHeader + red("No active teleport request from ") + aqua(source.displayName)
            )
            return 0
        }
        TeleportRequest.ACTIVE_REQUESTS[source.uuidAsString + context.source.player.uuidAsString]!!.notifySourceOfDeny()
        TeleportRequest.ACTIVE_REQUESTS.remove(source.uuidAsString + context.source.player.uuidAsString)
        context.source.sendFeedback(
            messageHeader + darkPurple("Teleport request from ") + aqua(source.displayName) + darkPurple(" was denied"),
            true
        )
        return 1
    }
}
