package me.gserv.fabrikommander.extension

import me.gserv.fabrikommander.coolDown.CoolDownType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.log
import me.gserv.fabrikommander.utils.ranks
import net.minecraft.server.network.ServerPlayerEntity
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
