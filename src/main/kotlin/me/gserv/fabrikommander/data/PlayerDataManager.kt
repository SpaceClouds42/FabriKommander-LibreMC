package me.gserv.fabrikommander.data

import com.charleskorn.kaml.UnknownPropertyException
import com.charleskorn.kaml.Yaml
import me.gserv.fabrikommander.coolDown.CoolDownType
import me.gserv.fabrikommander.data.spec.Home
import me.gserv.fabrikommander.data.spec.Player
import me.gserv.fabrikommander.data.spec.Pos
import me.gserv.fabrikommander.data.spec.old.OldPlayer
import me.gserv.fabrikommander.SERVER
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.WorldSavePath
import org.apache.logging.log4j.LogManager
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*
import kotlin.NoSuchElementException

object PlayerDataManager {
    private val logger = LogManager.getLogger(this::class.java)

    private var cache: MutableMap<UUID, Player> = mutableMapOf()

    private lateinit var dataDir: Path

    fun setup() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            SERVER = it
            cache.clear()
            dataDir = it.getSavePath(WorldSavePath.ROOT).resolve("FabriKommander")

            dataDir.toFile().mkdir()

            logger.info("Data directory: $dataDir")
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            shutdown()
        }
    }

    fun playerJoined(player: ServerPlayerEntity) {
        val uuid = player.uuid

        cache[uuid] = loadData(player)
    }

    fun playerLeft(player: ServerPlayerEntity) {
        val uuid = player.uuid
        setLogPos(uuid, Pos(player.x, player.y, player.z, player.yaw, player.pitch, player.world.registryKey.value))

        val data = cache[uuid]

        if (data != null) {
            saveData(uuid)
            cache.remove(uuid)
        }
    }

    fun loadData(player: ServerPlayerEntity): Player {
        val uuid = player.uuid
        val playerFile = dataDir.resolve("$uuid.yaml").toFile()

        if (!playerFile.exists()) {
            playerFile.createNewFile()

            val data = Player(name = player.gameProfile.name)

            playerFile.writeText(
                Yaml.default.encodeToString(Player.serializer(), data)
            )

            return data
        }

        return try {
            // Trying to deserialize the Player object normally
            val string = playerFile.readText()
            Yaml.default.decodeFromString(Player.serializer(), string)
        } catch (propError: UnknownPropertyException) {
            // region YTG1234: Probably the most cursed piece of code I've ever made, excluding TeleportRequest
            // That failed
            logger.error("Unknown properties found, attempting to convert from old format...")
            try {
                // Trying to deserialize from the old format and use the extension function to convert to a new player
                val oldString = playerFile.readText()
                val newPlayer = Yaml.default.decodeFromString(OldPlayer.serializer(), oldString).toNewPlayer()
                // If we got to this line, it didn't fail
                logger.warn("File " + playerFile.name + " was using the old format.")
                // Returning the correct thing
                newPlayer
            } catch (oldPropError: UnknownPropertyException) {
                // It's not in the old format either 🤷
                logger.fatal("File " + playerFile.name + " is using an invalid format!")
                throw oldPropError
            }
            // endregion
        }
    }

    fun saveData(uuid: UUID) {
        val playerFile = dataDir.resolve("$uuid.yaml").toFile()
        val data = cache[uuid]
            ?: throw NoSuchElementException("No cached data found for player: ($uuid)")

        if (!playerFile.exists()) {
            playerFile.createNewFile()
        }

        playerFile.writeText(
            Yaml.default.encodeToString(Player.serializer(), data)
        )
    }

    fun getHomes(uuid: UUID): List<Home>? {
        return cache[uuid]?.homes
    }

    fun getHome(uuid: UUID, name: String): Home? {
        return cache[uuid]?.homes
            ?.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }

    fun getHomeLimit(uuid: UUID): Int? {
        return cache[uuid]?.homeLimit
    }

    fun getRank(uuid: UUID): String? {
        return cache[uuid]?.rank
    }

    fun getNick(uuid: UUID): String? {
        return cache[uuid]?.nick
    }

    fun getCoolDownSetAt(uuid: UUID, coolDownType: CoolDownType): LocalDateTime? {
        return LocalDateTime.parse(cache[uuid]?.coolDowns?.getOrDefault(coolDownType, LocalDateTime.MIN.toString()))
    }

    fun getLogPos(uuid: UUID): Pos? {
        return cache[uuid]?.lastLogPos
    }

    fun setLogPos(uuid: UUID, pos: Pos) {
        cache[uuid]?.lastLogPos = pos

        saveData(uuid)
    }

    fun setCoolDown(uuid: UUID, coolDownType: CoolDownType) {
        cache[uuid]?.coolDowns?.set(coolDownType, LocalDateTime.now().toString())

        saveData(uuid)
    }

    fun resetCoolDown(uuid: UUID, coolDownType: CoolDownType) {
        cache[uuid]?.coolDowns?.set(coolDownType, LocalDateTime.MIN.toString())

        saveData(uuid)
    }

    fun isInStaffChat(uuid: UUID): Boolean? {
        return cache[uuid]?.inStaffChat
    }

    fun toggleStaffChat(uuid: UUID) {
        cache[uuid]?.inStaffChat = !isInStaffChat(uuid)!!

        saveData(uuid)
    }

    fun setInStaffChat(uuid: UUID, newInStaffChat: Boolean) {
        cache[uuid]?.inStaffChat = newInStaffChat

        saveData(uuid)
    }

    fun setNick(uuid: UUID, newNick: String?) {
        cache[uuid]?.nick = newNick
        SERVER.playerManager.sendToAll(PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, SERVER.playerManager.getPlayer(uuid)))
        saveData(uuid)
    }

    fun setRank(uuid: UUID, newRank: String) {
        cache[uuid]?.rank = newRank
        SERVER.playerManager.sendToAll(PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, SERVER.playerManager.getPlayer(uuid)))
        saveData(uuid)
    }

    fun setHomeLimit(uuid: UUID, newHomeLimit: Int) {
        cache[uuid]?.homeLimit = newHomeLimit

        saveData(uuid)
    }

    fun setHome(uuid: UUID, home: Home): Boolean? {
        val existing = getHome(uuid, home.name)

        if (existing != null) {
            cache[uuid]?.homes?.remove(home)
        }

        val result = cache[uuid]?.homes?.add(home)

        saveData(uuid)

        return result
    }

    fun deleteHome(uuid: UUID, name: String): Boolean? {
        val result = cache[uuid]?.homes?.removeIf { it.name.equals(name, ignoreCase = true) }

        saveData(uuid)

        return result
    }

    fun getBackPos(uuid: UUID): Pos? = cache[uuid]?.backPos

    fun setBackPos(uuid: UUID, newPos: Pos) {
        cache[uuid]?.backPos = newPos
        saveData(uuid)
    }

    fun shutdown() {
        for (uuid in cache.keys) {
            saveData(uuid)
        }
    }
}
