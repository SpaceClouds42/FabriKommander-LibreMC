package me.gserv.fabrikommander

import me.gserv.fabrikommander.commands.*
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.utils.Dispatcher
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import org.apache.logging.log4j.LogManager


object Common : ModInitializer {
    private val logger = LogManager.getLogger(this::class.java)

    override fun onInitialize() {
        PlayerDataManager.setup()

        CommandRegistrationCallback.EVENT.register(::registerCommands)
    }

    fun registerCommands(dispatcher: Dispatcher, dedicated: Boolean) {
        logger.debug("Registering commands.")

        // Home management commands
        DelHomeCommand(dispatcher).register()
        GetHomeCommand(dispatcher).register()
        HomeCommand(dispatcher).register()
        HomesCommand(dispatcher).register()
        SetHomeCommand(dispatcher).register()

        // TPA commands
        TpaCommand(dispatcher).register()
        TpaHereCommand(dispatcher).register()
        TpAcceptCommand(dispatcher).register()
        TpCancelCommand(dispatcher).register()
        TpDenyCommand(dispatcher).register()
        
        // Teleport commands
        BackCommand(dispatcher).register()
        RtpCommand(dispatcher).register()

        // Misc commands
        PingCommand(dispatcher).register()
        DieCommand(dispatcher).register()
    }
}
