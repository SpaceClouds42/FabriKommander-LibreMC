package me.gserv.fabrikommander.coolDown

import me.gserv.fabrikommander.data.PlayerDataManager
import net.minecraft.server.network.ServerPlayerEntity
import java.time.Duration
import java.time.LocalDateTime

interface CoolDown {
    val type: CoolDowns
    val duration: Duration

    fun getCoolDown(player: ServerPlayerEntity): Duration? {
        return this.duration - Duration.between(PlayerDataManager.getCoolDownSetAt(player.uuid, this.type), LocalDateTime.now())
    }

    fun isCoolDownOver(player: ServerPlayerEntity): Boolean? {
        return this.duration <= Duration.between(PlayerDataManager.getCoolDownSetAt(player.uuid, this.type), LocalDateTime.now())
    }
}