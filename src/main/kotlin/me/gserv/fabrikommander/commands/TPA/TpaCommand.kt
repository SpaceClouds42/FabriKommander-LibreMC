package me.gserv.fabrikommander.commands.TPA

import me.gserv.fabrikommander.data.TeleportRequest
import me.gserv.fabrikommander.utils.*
import net.minecraft.util.Util
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.network.ServerPlayerEntity

class TpaCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            literal("tpa").then(
                argument("target", EntityArgumentType.player()).executes(this::tpaCommand)
            )
        )
    }

    fun isTeleportRequestUnique(source: ServerPlayerEntity, target: ServerPlayerEntity): Boolean {
        val id = source.uuidAsString + target.uuidAsString
        return !TeleportRequest.ACTIVE_REQUESTS.containsKey(id)
    }

    fun tpaCommand(context: Context): Int {
        val target = EntityArgumentType.getPlayer(context, "target")
        val source = context.source.player
        val unique = isTeleportRequestUnique(source, target)
        val messageHeader = gray("[") + yellow("TPA") + gray("] ") + reset("")
        if (unique) {
            val request = TeleportRequest(source = source, target = target, inverted = false)
            request.notifyTargetOfRequest()
            context.source.sendFeedback(
                messageHeader +
                        aqua("Teleport request sent to ") + target.displayName +
                        reset(" ") + hover(
                    click(
                        red("[X]"),
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/tpcancel " + target.entityName // There can be multiple active requests
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        red("Click here to cancel the request.")
                    )
                ) + reset(""),
                true
            )
        } else {
            context.source.sendFeedback(
                messageHeader +
                        aqua("You already have a teleport request to ") + target.displayName +
                        reset(" ") + hover(
                    click(
                        red("[X]"),
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/tpcancel " + target.entityName // There can be multiple active requests
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        red("Click here to cancel the existing request.")
                    )
                ) + reset(""),
                true
            )
        }
        return 1
    }
}
