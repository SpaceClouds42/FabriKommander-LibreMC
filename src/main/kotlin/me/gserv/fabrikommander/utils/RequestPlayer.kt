package me.gserv.fabrikommander.utils

import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.server.command.ServerCommandSource
import com.mojang.brigadier.context.CommandContext
import me.gserv.fabrikommander.SERVER
import me.gserv.fabrikommander.data.PlayerDataManager

@Deprecated(
    "CommandContext is no longer needed",
    replaceWith = ReplaceWith(
        "requestPlayer(requestedProfile)",
        "import me.gserv.fabrikommander.utils.requestPlayer"
    )
)
@Throws(CommandSyntaxException::class)
fun requestPlayer(context: CommandContext<ServerCommandSource>, requestedProfile: GameProfile): ServerPlayerEntity {
    return requestPlayer(requestedProfile)
}

@Throws(CommandSyntaxException::class)
fun requestPlayer(requestedProfile: GameProfile): ServerPlayerEntity {
    val minecraftServer = SERVER
    var requestedPlayer = minecraftServer.playerManager.getPlayer(requestedProfile.name)

    if (requestedPlayer == null) {
        requestedPlayer = minecraftServer.playerManager.createPlayer(requestedProfile)
        minecraftServer.playerManager.loadPlayerData(requestedPlayer)
    }
    PlayerDataManager.playerJoined(requestedPlayer!!)
    return requestedPlayer
}