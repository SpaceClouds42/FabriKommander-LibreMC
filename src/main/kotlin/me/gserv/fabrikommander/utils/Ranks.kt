package me.gserv.fabrikommander.utils

import me.gserv.fabrikommander.data.PlayerDataManager
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.core.Logger
import org.apache.logging.log4j.LogManager

val ranks = listOf(
    "member",
    "VIP",
    "VIP+",
    "MVP",
    "MVP+",
    "Builder",
    "Helper",
    "Mod",
    "Dev",
    "Owner"
)

val staffRanks = listOf(
    "Builder",
    "Helper",
    "Mod",
    "Dev",
    "Owner"
)

val rankToHomeLimit = hashMapOf(
    "member" to 3,
    "VIP" to 5,
    "VIP+" to 8,
    "MVP" to 12,
    "MVP+" to 20,
    "Builder" to 8,
    "Helper" to 8,
    "Mod" to 8,
    "Dev" to 8,
    "Owner" to 8
)

val LOGGER = LogManager.getLogger("FabriKommander-LibreMC") as Logger


fun hasRankPermissionLevel(player: ServerPlayerEntity, rank: String): Boolean {
    if (!ranks.contains(rank)) {
        LOGGER.info("ERROR: Ranks.kt; Rank '$rank' not found, a command or event must be checking for a rank permission level with the wrong rank name.")
        return false
    }
    return ranks.indexOf(PlayerDataManager.getRank(player.uuid)) >= ranks.indexOf(rank)
}