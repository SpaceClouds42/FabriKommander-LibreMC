package me.gserv.fabrikommander.data

import me.gserv.fabrikommander.utils.aqua
import me.gserv.fabrikommander.utils.click
import me.gserv.fabrikommander.utils.green
import me.gserv.fabrikommander.utils.hover
import me.gserv.fabrikommander.utils.plus
import me.gserv.fabrikommander.utils.red
import me.gserv.fabrikommander.utils.reset
import me.gserv.fabrikommander.utils.yellow
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText
import net.minecraft.util.Util

class TeleportRequest(
    val source: ServerPlayerEntity,
    val target: ServerPlayerEntity,
    val inverted: Boolean
) {
    companion object {
        @JvmStatic
        val ACTIVE_REQUESTS = hashMapOf<String, TeleportRequest>()
    }

    init {
        // I should come up with a better system but I want to make sure that there can't be two teleport requests with the same source and target.
        val id = source.uuidAsString + target.uuidAsString
        ACTIVE_REQUESTS[id] = this
    }

    fun apply() {
        if (inverted) {
            PlayerDataManager.setBackPos(
                target.uuid, Pos( 
                    world = target.world.registryKey.value,
    
                    x = target.x,
                    y = target.y,
                    z = target.z,
    
                    yaw = target.yaw,
                    pitch = target.pitch
                )
            )
            target.teleport(source.serverWorld, source.x, source.y, source.z, source.yaw, source.pitch)
        } else {
            PlayerDataManager.setBackPos(
                source.uuid, Pos( 
                    world = source.world.registryKey.value,
    
                    x = source.x,
                    y = source.y,
                    z = source.z,
    
                    yaw = source.yaw,
                    pitch = source.pitch
                )
            )
            source.teleport(target.serverWorld, target.x, target.y, target.z, target.yaw, target.pitch)
        }
    }

    fun notifySourceOfAccept() {
        val message =
            reset("") + target.displayName + aqua(" has accepted your teleport request")
        source.sendSystemMessage(message, Util.NIL_UUID)
    }

    fun notifySourceOfDeny() {
        val message = reset("") + target.displayName + aqua(" has denied your teleport request")
        source.sendSystemMessage(message, Util.NIL_UUID)
    }

    fun notifyTargetOfRequest() {
        // Message will be configurable later
        val message =
            reset("") + source.displayName as MutableText + yellow( // reset("") used to make the vanilla click event for player names not apply to the whole message
                " has requested " + when (inverted) {
                    true -> "you teleport to them"
                    false -> "to teleport to you"
                }
            ) + reset(". [") + click(
                hover(
                    aqua("Accept"),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        green("Click here to accept the request.")
                    )
                ),
                ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/tpaccept " + source.entityName // There can be multiple active requests
                )
            ) + reset(" / ") + hover(
                click(
                    aqua("Deny"),
                    ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/tpdeny " + source.entityName // There can be multiple active requests
                    )
                ),
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    red("Click here to deny the request.")
                )
            ) + reset("]")

        target.sendSystemMessage(message, Util.NIL_UUID)
    }

    fun notifyTargetOfCancel() {
        val message = reset("") + source.displayName + aqua(" has cancelled their teleport request.")
        target.sendSystemMessage(message, Util.NIL_UUID)
    }
}
