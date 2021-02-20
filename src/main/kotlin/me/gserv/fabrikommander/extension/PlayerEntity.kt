package me.gserv.fabrikommander.extension

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText

fun PlayerEntity.nickName(): MutableText {
    PlayerDataManager.playerJoined(this as ServerPlayerEntity)

    val prefix = if (rankToPrefix[PlayerDataManager.getRank(this.uuid)]?.shallowCopy() == null) {
        reset("")
    } else {
        rankToPrefix[PlayerDataManager.getRank(this.uuid)]!!.shallowCopy()
    }
    val name: MutableText
    val nick = PlayerDataManager.getNick(this.uuid)
    if (nick == null) {
        name = reset("") +
                hover(
                    click(
                        gray(this.entityName),
                        ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            "/tell ${this.entityName}"
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        white(this.entityName)
                    )
                ) +
                reset("")
    } else {
        name = reset("") +
                hover(
                    click(
                        reset(nick),
                        ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            "/tell ${this.entityName}"
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        white(this.entityName)
                    )
                ) +
                reset("")
    }

    return reset("") + prefix + name + reset("")
}