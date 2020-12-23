package me.gserv.fabrikommander.commands

import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.text.HoverEvent
import net.minecraft.util.Util

class RulesCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("rules")
                .executes { rulesCommand(it) }
        )
    }

    fun rulesCommand(context: Context): Int {
        val rule1 = hover(
            darkAqua("1. No hacking of any kind."),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                red("This includes any modification that gives you an unfair advantage.")
            )
        )
        val rule2 = hover(
            darkAqua("2. No exploits of any kind."),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                red("This includes item duping, coord exploits, seed exploits, any abuse of plugins, etc.")
            )
        )
        val rule3 = hover(
            darkAqua("3. No spamming in chat!"),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                red("This is only punished with mutes of increasing duration")
            )
        )
        val rule4 = hover(
            darkAqua("4. No racism or homophobia of any kind."),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                red("This includes harassment of players due to race or sexual orientation and use of bigoted words, such as the n-word and the f-slur")
            )
        )
        val rule5 = hover(
            darkAqua("5. No advertisements."),
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                red("This is only punished with mutes of increasing duration")
            )
        )
        val punishmentsHeader = darkRed(underline(bold("Punishments")))
        val punishments = darkGreen(reset("First offense: warning")) +
                yellow(reset("\nSecond offense: 1 day ban")) +
                gold(reset("\nThird offense: 1 week ban")) +
                red(reset("\nFourth offense: Permanent ban"))
        val enter = reset("\n")

        context.source.player.sendSystemMessage(
            rule1,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
           rule2,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rule3,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rule4,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            rule5,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            enter + punishmentsHeader,
            Util.NIL_UUID
        )
        context.source.player.sendSystemMessage(
            punishments,
            Util.NIL_UUID
        )


        return 1
    }
}