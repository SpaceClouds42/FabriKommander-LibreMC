package me.gserv.fabrikommander

import me.gserv.fabrikommander.commands.*
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.SpawnDataManager
import me.gserv.fabrikommander.data.WarpDataManager
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.TablistVariables
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager


object Common : ModInitializer {
    private val logger = LogManager.getLogger(this::class.java)

    override fun onInitialize() {
        PlayerDataManager.setup()
        SpawnDataManager.setup()
        WarpDataManager.setup()

        CommandRegistrationCallback.EVENT.register(::registerCommands)
        ServerTickEvents.END_SERVER_TICK.register(::onTick)
    }

    fun registerCommands(dispatcher: Dispatcher, dedicated: Boolean) {
        logger.debug("Registering commands.")

        // Home management commands
        DelHomeCommand(dispatcher).register()
        GetHomeCommand(dispatcher).register()
        HomeCommand(dispatcher).register()
        HomeLimitCommand(dispatcher).register()
        HomesCommand(dispatcher).register()
        SetHomeCommand(dispatcher).register()

        // Warp management commands
        DelWarpCommand(dispatcher).register()
        GetWarpCommand(dispatcher).register()
        SetWarpCommand(dispatcher).register()
        WarpCommand(dispatcher).register()
        WarpsCommand(dispatcher).register()

        // TPA commands
        TpaCommand(dispatcher).register()
        TpaHereCommand(dispatcher).register()
        TpAcceptCommand(dispatcher).register()
        TpCancelCommand(dispatcher).register()
        TpDenyCommand(dispatcher).register()
        
        // Teleport commands
        BackCommand(dispatcher).register()
        RtpCommand(dispatcher).register()
        SpawnCommand(dispatcher).register()
        SetSpawnCommand(dispatcher).register()

        // Misc commands
        DieCommand(dispatcher).register()

        // Text commands
        DiscordCommand(dispatcher).register()
        InfoCommand(dispatcher).register()
        PingCommand(dispatcher).register()
        DisplayItemCommand(dispatcher).register()
        FormatCommand(dispatcher).register()
        RulesCommand(dispatcher).register()
        VoteCommand(dispatcher).register()
        InfoCommand(dispatcher).register()
        RanksCommand(dispatcher).register()
        StaffChatCommand(dispatcher).register()

        // Donor commands
        RankCommand(dispatcher).register()
    }

    fun onTick(minecraftServer: MinecraftServer) {
        TablistVariables.INSTANCE.onTick(minecraftServer)
    }
}
