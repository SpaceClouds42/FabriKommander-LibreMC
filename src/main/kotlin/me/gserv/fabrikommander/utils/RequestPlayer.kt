package me.gserv.fabrikommander.utils

import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.server.command.ServerCommandSource
import com.mojang.brigadier.context.CommandContext
import me.gserv.fabrikommander.data.PlayerDataManager


@Throws(CommandSyntaxException::class)
fun requestPlayer(context: CommandContext<ServerCommandSource>, requestedProfile: GameProfile): ServerPlayerEntity {
    val minecraftServer = context.source.minecraftServer
    var requestedPlayer = minecraftServer.playerManager.getPlayer(requestedProfile.name)

    if (requestedPlayer == null) {
        requestedPlayer = minecraftServer.playerManager.createPlayer(requestedProfile)
        minecraftServer.playerManager.loadPlayerData(requestedPlayer)
    }
    PlayerDataManager.playerJoined(requestedPlayer!!)
    return requestedPlayer
}