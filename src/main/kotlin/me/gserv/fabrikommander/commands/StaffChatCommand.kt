package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager

class StaffChatCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("staffchat")
                .requires { hasRankPermissionLevel(it.player, "Builder") }
                .executes { staffChatCommand(it) }
        )
    }

    fun staffChatCommand(context: Context): Int {
        val player = context.source.player

        PlayerDataManager.toggleStaffChat(player.uuid)

        context.source.sendFeedback(
            when (PlayerDataManager.isInStaffChat(player.uuid)!!) {
                true -> gray("[") + yellow("Staff") + gray("] ") + green("Joined Staff Chat")
                false -> gray("[") + yellow("Staff") + gray("] ") + red("Left Staff Chat")
            },
            false
        )

        return 1
    }
}