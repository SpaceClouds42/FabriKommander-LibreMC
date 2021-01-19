package me.gserv.fabrikommander.extension

import me.gserv.fabrikommander.coolDown.CoolDownType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText
import java.time.Duration
import java.time.LocalDateTime

fun ServerPlayerEntity.hasRankPermissionLevel(rank: String): Boolean {
    if (!ranks.contains(rank)) {
        log("rank.main", "ERROR: Ranks.kt; Rank '$rank' not found, a command or event must be checking for a rank permission level with the wrong rank name.")
        return false
    }
    return ranks.indexOf(PlayerDataManager.getRank(this.uuid)) >= ranks.indexOf(rank)
}

fun ServerPlayerEntity.getCoolDownExpiryDuration(coolDownType: CoolDownType): Duration? {
    return coolDownType.duration - Duration.between(PlayerDataManager.getCoolDownSetAt(this.uuid, coolDownType), LocalDateTime.now())
}

fun ServerPlayerEntity.isCoolDownOver(coolDownType: CoolDownType): Boolean {
    return coolDownType.duration <= Duration.between(PlayerDataManager.getCoolDownSetAt(this.uuid, coolDownType), LocalDateTime.now())
}

fun ServerPlayerEntity.nickName(): MutableText {
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
