package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.LiteralText
import net.minecraft.util.Util

class FormatCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("format")
                .executes { formatCommand(it) }
        )
    }

    fun formatCommand(context: Context): Int {
        val codes = LiteralText("&7Use the \"&\" character following one fo the formatting codes for your text to be formatted. Use \\ to escape format codes.&r\n" +
                " &00 &11 &22 &33 &44 &55 &66 &77 &88 &99&r\n" +
                " &aa &bb &cc &dd &ee &ff&r\n" +
                "&7Font codes:&r\n k: &kobfuscated&r\n l: &lbold&r\n m: &mstrikethrough&r\n n: &nunderline&r\n o: &oitalic&r\n r: &rreset&r\n" +
                "&7Custom variables can be accessed by placing \"#\" in front of these codes:&r\n" +
                " N: new line\n UPTIME: formatted uptime\n TPS: ticks per second\n MSPT: milliseconds per tick"
        )

        context.source.player.sendSystemMessage(
            codes,
            Util.NIL_UUID
        )

        return 1
    }
}