package me.gserv.fabrikommander.commands.donor

import me.gserv.fabrikommander.data.PlayerDataManager
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.extension.hasRankPermissionLevel
import me.gserv.fabrikommander.utils.Context
import me.gserv.fabrikommander.utils.Dispatcher
import me.gserv.fabrikommander.utils.Node
import me.gserv.fabrikommander.utils.red
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager
import net.minecraft.util.math.Vec3d

class BedHomeCommand(val dispatcher: Dispatcher) {
    fun register() {
        val bedHomeNode: Node =
            CommandManager
                .literal("bedhome")
                .requires { it.player.hasRankPermissionLevel("VIP") }
                .executes { bedHomeCommand(it) }
                .build()

        dispatcher.root.addChild(bedHomeNode)
    }

    fun bedHomeCommand(context: Context): Int {
        val player = context.source.player

        val world = player.server.getWorld(player.spawnPointDimension)
        val respawnBlockPos = player.spawnPointPosition
        val spawnAngle = player.spawnAngle
        val spawnPosSet = player.isSpawnPointSet
        val alive = true
        if (world == null || respawnBlockPos == null) {
            context.source.sendError(
                red("Respawn position not found")
            )
            return 0
        }
        val optionalRespawnVec3d =
            PlayerEntity.findRespawnPosition(world, respawnBlockPos, spawnAngle, spawnPosSet, alive)
        if (!optionalRespawnVec3d.isPresent) {
            context.source.sendError(
                red("Respawn position not found")
            )
            return 0
        }
        val respawnVec3d = optionalRespawnVec3d.get()
        PlayerDataManager.setBackPos(
            player.uuid,
            Pos(player.x, player.y, player.z, player.yaw, player.pitch, player.world.registryKey.value)
        )
        player.teleport(world, respawnVec3d.x, respawnVec3d.y, respawnVec3d.z, player.yaw, player.pitch)
        return 1
    }
}