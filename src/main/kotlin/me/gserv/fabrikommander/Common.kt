package me.gserv.fabrikommander
//TODO: import purge
import me.gserv.fabrikommander.commands.donor.*
import me.gserv.fabrikommander.commands.homeManagement.*
import me.gserv.fabrikommander.commands.misc.*
import me.gserv.fabrikommander.commands.staff.*
import me.gserv.fabrikommander.commands.teleport.*
import me.gserv.fabrikommander.commands.text.*
import me.gserv.fabrikommander.commands.TPA.*
import me.gserv.fabrikommander.commands.warpManagement.*
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

        // Donor commands
        AnvilCommand(dispatcher).register()
        CraftCommand(dispatcher).register()
        EnderChestCommand(dispatcher).register()
        GlintCommand(dispatcher).register()
        HatCommand(dispatcher).register()
        NickCommand(dispatcher).register()

        // Home management commands
        DelHomeCommand(dispatcher).register()
        GetHomeCommand(dispatcher).register()
        HomeCommand(dispatcher).register()
        HomeLimitCommand(dispatcher).register()
        HomesCommand(dispatcher).register()
        SetHomeCommand(dispatcher).register()

        // Misc commands
        DieCommand(dispatcher).register()

        // Staff commands
        StaffChatCommand(dispatcher).register()
        RankCommand(dispatcher).register()
        
        // Teleport commands
        BackCommand(dispatcher).register()
        RtpCommand(dispatcher).register()

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

        // TPA commands
        TpaCommand(dispatcher).register()
        TpaHereCommand(dispatcher).register()
        TpAcceptCommand(dispatcher).register()
        TpCancelCommand(dispatcher).register()
        TpDenyCommand(dispatcher).register()

        // Warp management commands
        DelWarpCommand(dispatcher).register()
        GetWarpCommand(dispatcher).register()
        SetWarpCommand(dispatcher).register()
        WarpCommand(dispatcher).register()
        WarpsCommand(dispatcher).register()
        SpawnCommand(dispatcher).register()
        SetSpawnCommand(dispatcher).register()
    }

    fun onTick(minecraftServer: MinecraftServer) {
        TablistVariables.INSTANCE.onTick(minecraftServer)
        // RNG manipulation patch
        for (player in minecraftServer.playerManager.playerList) {
            player.random.nextInt()
        }
    }
}
