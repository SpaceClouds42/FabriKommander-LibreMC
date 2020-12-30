package me.gserv.fabrikommander.commands

import com.mojang.brigadier.arguments.StringArgumentType
import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.spec.Home
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.utils.*
import net.minecraft.server.command.CommandManager

class SetHomeCommand(val dispatcher: Dispatcher) {
    fun register() {
        dispatcher.register(
            CommandManager.literal("sethome")
                .executes { setHomeCommand(it) }
                .then(
                    CommandManager.argument("name", StringArgumentType.word())
                        .executes { setHomeCommand(it, StringArgumentType.getString(it, "name")) }
                )
        )
    }

    fun isPlayerHomeLimitReached(context: Context): Boolean {
        val player = context.source.player
        val homes = PlayerDataManager.getHomes(player.uuid)
        val homeCount = homes!!.size
        var homeLimit = PlayerDataManager.getHomeLimit(player.uuid)
        if (homeLimit == 0) {
            return false
        }
        return (homeCount >= homeLimit!!)
    }

    fun setHomeCommand(context: Context, name: String = "home"): Int {
        val player = context.source.player

        val home = Home(
            name = name,

            pos = Pos(
                x = player.x,
                y = player.y,
                z = player.z,
                yaw = player.yaw,
                pitch = player.pitch,
                world = player.world.registryKey.value
            ),
        )

        if (!isPlayerHomeLimitReached(context)) {
            PlayerDataManager.setHome(player.uuid, home)

            context.source.sendFeedback(
                green("Home created: ") + aqua(name),
                true
            )
        } else {
            var homeLimit = PlayerDataManager.getHomeLimit(player.uuid)

            context.source.sendFeedback(
                red("You already have $homeLimit homes!"),
                false
            )
        }

        return 1
    }
}
