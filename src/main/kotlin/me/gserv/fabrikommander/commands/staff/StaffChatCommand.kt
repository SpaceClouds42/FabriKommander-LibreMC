package me.gserv.fabrikommander.commands.staff

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.PlayerDataManager.getRank
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util
import java.util.function.Consumer

class StaffChatCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("staffchat")
                .requires { hasRankPermissionLevel(it.player, "Builder") }
                .executes { staffChatCommand(it) }
                .then(
                    CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes { staffChatCommand(it, StringArgumentType.getString(it, "message")) }
                )
        )
    }

    fun staffChatCommand(context: Context): Int {
        val player = context.source.player

        PlayerDataManager.toggleStaffChat(player.uuid)

        context.source.sendFeedback(
            when (PlayerDataManager.isInStaffChat(player.uuid)!!) {
                true -> gray("[") + yellow("Staff") + gray("] ") + green("Entered Staff Chat")
                false -> gray("[") + yellow("Staff") + gray("] ") + red("Left Staff Chat")
            },
            false
        )

        return 1
    }

    fun staffChatCommand(context: Context, message: String): Int {
        val player = context.source.player
        val staffChatMessage = gray("[") +
            yellow("Staff") +
            gray("] ") +
            gray(player.entityName) +
            darkGray(bold(" > ")) +
            gray(message)

        player.server.playerManager.playerList.forEach(Consumer { p: ServerPlayerEntity ->
                if (staffRanks.contains(getRank(p.uuid))) {
                    p.sendSystemMessage(staffChatMessage, Util.NIL_UUID)
                }
            }
        )
        log("chat.staff", "${player.entityName} > $message")

        return 1
    }
}