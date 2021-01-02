package me.gserv.fabrikommander.data

import com.charleskorn.kaml.Yaml
import me.gserv.fabrikommander.data.spec.*
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.util.WorldSavePath
import org.apache.logging.log4j.LogManager
import java.nio.file.Path

object WarpDataManager {
    private val logger = LogManager.getLogger(this::class.java)

    private var warps: MutableList<Warp> = mutableListOf()

    private lateinit var dataDir: Path

    fun setup() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            warps.clear()

            dataDir = it.getSavePath(WorldSavePath.ROOT).resolve("FabriKommander")

            dataDir.toFile().mkdir()

            logger.info("Data directory: $dataDir")

            warps = loadData().warps
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            shutdown()
        }
    }

    fun loadData(): Warps {
        val warpsFile = dataDir.resolve("warps.yaml").toFile()

        if (!warpsFile.exists()) {
            warpsFile.createNewFile()

            val data = Warps()

            warpsFile.writeText(
                Yaml.default.encodeToString(Warps.serializer(), data)
            )

            return data
        }

        val string = warpsFile.readText()
        return Yaml.default.decodeFromString(Warps.serializer(), string)
    }

    fun saveData() {
        val warpsFile = dataDir.resolve("warps.yaml").toFile()
        val data = Warps(warps)

        if (!warpsFile.exists()) {
            warpsFile.createNewFile()
        }

        warpsFile.writeText(
            Yaml.default.encodeToString(Warps.serializer(), data)
        )
    }

    fun getWarps(): List<Warp>? {
        return loadData().warps
    }

    fun getWarp(name: String): Warp? {
        return getWarps()
            ?.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }

    fun setWarp(warp: Warp): Boolean? {
        val existing = getWarp(warp.name)

        if (existing != null) {
            deleteWarp(warp.name)
        }

        val result = warps.add(warp)

        saveData()

        return result
    }

    fun deleteWarp(name: String): Boolean? {
        val result = warps.removeIf { it.name.equals(name, ignoreCase = true) }

        saveData()

        return result
    }

    fun shutdown() {
        saveData()
    }
}