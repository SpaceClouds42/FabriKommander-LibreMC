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

class TpaHereCommand(val dispatcher: Dispatcher) {
    val messageHeader = gray("[") + yellow("TPA") + gray("] ") + reset("")

    fun register() {
        dispatcher.register(
            literal("tpahere").then(
                argument("target", EntityArgumentType.player()).executes(this::tpaHereCommand)
            )
        )
    }

    fun tpaHereCommand(context: Context): Int {
        val target = EntityArgumentType.getPlayer(context, "target")
        val source = context.source.player
        val request = TeleportRequest(source = source, target = target, inverted = true)
        request.notifyTargetOfRequest()
        context.source.sendFeedback(
            messageHeader + 
            darkPurple("Teleport request sent to ") + aqua(target.displayName.asString()),
            true
        )
        return 1
    }
}
