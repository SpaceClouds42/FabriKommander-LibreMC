package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.coolDown.CoolDownType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.extension.*
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager

//TODO: Donor command interface, should have "notDonor(player)" function
//TODO: CoolDownCommand interface, should have "val coolDown: CoolDowns" property
class GlintCommand(val dispatcher: Dispatcher) {
    fun register() {
        val glintNode: Node =
            CommandManager
                .literal("glint")
                .requires { it.player.hasRankPermissionLevel("VIP") }
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
        if (itemStack.count != 1) {
            context.source.sendError(
                red("Cannot glint multiple items at once")
            )
            return 0
        }
        if (itemStack.hasGlint()) {
            context.source.sendError(
                red("This item already has a glint")
            )
            return 0
        }
        if (player.isCoolDownOver(CoolDownType.GLINT)) {
            itemStack.applyGlint()
            context.source.sendFeedback(
                green("Glint added to ") +
                        aqua(itemStack.name.copy()),
                false
            )
            PlayerDataManager.setCoolDown(player.uuid, CoolDownType.GLINT)
            return 1
        } else {
            context.source.sendError(
                red("You cannot use this command for ")  +
                        gold(player.getCoolDownExpiryDuration(CoolDownType.GLINT)!!.prettyPrint())
            )
            return 0
        }
    }

    fun glintRemoveCommand(context: Context): Int {
        val player = context.source.player
        val itemStack = player.mainHandStack
        if (itemStack.isEmpty || itemStack == null) {
            context.source.sendError(
                red("Must hold an item to remove glint")
            )
            return 0
        }
        if (itemStack.count != 1) {
            context.source.sendError(
                red("Cannot remove glint from multiple items at once")
            )
            return 0
        }
        if (!itemStack.hasGlint()) {
            context.source.sendError(
                red("This item does not have a glint")
            )
            return 0
        }
        if (itemStack.hasGlint() && !itemStack.tag!!.getBoolean("Glint")) {
            context.source.sendError(
                red("This item's glint cannot be removed")
            )
            return 0
        }
        itemStack.removeGlint()
        context.source.sendFeedback(
            green("Glint removed from ") +
                    aqua(itemStack.name.copy()),
            false
        )
        PlayerDataManager.resetCoolDown(player.uuid, CoolDownType.GLINT)
        return 1
    }
}