package me.gserv.fabrikommander.commands.misc

import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.red
import net.minecraft.server.command.CommandManager
import net.minecraft.text.LiteralText

class DieCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("die")
                .executes { dieCommand(it) }
        )
    }

    fun dieCommand(context: Context): Int {
        context.source.player.kill()
        context.source.sendFeedback(
            red("You died"),
            false
        )

        return 1
    }
}