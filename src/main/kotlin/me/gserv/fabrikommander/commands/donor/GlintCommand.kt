package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.coolDown.CoolDowns
import me.gserv.fabrikommander.coolDown.GlintCoolDown
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.TranslatableText

class GlintCommand(val dispatcher: Dispatcher) {
    fun register() {
        val glintNode: Node =
            CommandManager
                .literal("glint")
                .requires { hasRankPermissionLevel(it.player, "VIP") }
                .executes { glintAddCommand(it) }
                .build()
        val removeNode: Node =
            CommandManager
                .literal("remove")
                .executes { glintRemoveCommand(it) }
                .build()

        dispatcher.root.addChild(glintNode)
        glintNode.addChild(removeNode)
    }
    fun glintAddCommand(context: Context): Int {
        val player = context.source.player
        val itemStack = player.mainHandStack
        if (itemStack.isEmpty || itemStack == null) {
            context.source.sendError(
                red("Must hold an item to glint it")
            )
            return 0
        }
        if (itemStack.hasGlint()) {
            context.source.sendError(
                red("This item already has a glint")
            )
            return 0
        }
        if (GlintCoolDown().isCoolDownOver(player) == true) {
            itemStack.orCreateTag.putBoolean("Glint", true)
            context.source.sendFeedback(
                green("Glint added to ") +
                        aqua(itemStack.name.asString()),
                false
            )
            PlayerDataManager.setCoolDown(player.uuid, CoolDowns.GLINT)
            return 1
        } else {
            context.source.sendError(
                red("You cannot use this command for ")  +
                        //TODO: Cooldown is not formatted properly
                        gold(GlintCoolDown().getCoolDown(player).toString())
            )
            return 0
        }
    }
    //TODO: Shouldn't successfully remove glint from default glints, should instead check for
    fun glintRemoveCommand(context: Context): Int {
        val player = context.source.player
        val itemStack = player.mainHandStack
        if (itemStack.isEmpty || itemStack == null) {
            context.source.sendError(
                red("Must hold an item to remove glint")
            )
            return 0
        }
        if (!itemStack.hasGlint()) {
            context.source.sendError(
                red("This item doesn't have a glint")
            )
            return 0
        }
        itemStack.orCreateTag.putBoolean("Glint", false)
        context.source.sendFeedback(
            green("Glint removed from ") +
                    aqua(itemStack.name.asString()),
            false
        )
        PlayerDataManager.resetCoolDown(player.uuid, CoolDowns.GLINT)
        return 1
    }

    private fun isCoolDownOver(player: ServerPlayerEntity): Boolean {
        return true
    }

    private fun getCoolDown(player: ServerPlayerEntity): MutableText {
        return reset("")
    }
}