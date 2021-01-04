package me.gserv.fabrikommander.utils

import net.minecraft.server.MinecraftServer
import java.lang.management.ManagementFactory
import kotlin.math.floor

class TablistVariables {
    var mspt = 1.0
    private var tps = 1.0


    fun onTick(minecraftServer: MinecraftServer) {
        mspt = minecraftServer.tickTime.toDouble()
        if (mspt != 0.0) {
            tps = 1000 / mspt
        }
    }

    fun getTPS(): Double {
        return tps.coerceAtMost(20.00)
    }

    fun getUptime(): String {
            var s = ""
            val time = ManagementFactory.getRuntimeMXBean().uptime.toInt() / 1000
            val days = floor((time / 86400f).toDouble()).toInt()
            val hours = floor((time / 3600f).toDouble()).toInt() - days * 24
            val minutes = floor((time / 60f).toDouble()).toInt() - hours * 60 - days * 1440
            val seconds = time - minutes * 60 - hours * 3600 - days * 86400
            if (days > 0) {
                s = s + days + " day" + (if (days == 1) "" else "s") + ", "
            }
            if (hours > 0 || days > 0) {
                s = s + (if (hours < 10) "0" else "") + hours + ":"
            }
            if (minutes > 0 || hours > 0 || days > 0) {
                s = s + (if (minutes < 10) "0" else "") + minutes + ":"
            }
            s = s + (if (seconds < 10) "0" else "") + seconds
            return s
    }

    companion object {
        val INSTANCE = TablistVariables()
    }
}

