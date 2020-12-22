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
import me.gserv.fabrikommander.utils.reset
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
                red("No active teleport request from ") + aqua(source.entityName)
            )
            return 0
        }
        request.apply()
        request.notifySourceOfAccept()
        TeleportRequest.ACTIVE_REQUESTS.remove(source.uuidAsString + context.source.player.uuidAsString)
        return 1
    }
}
