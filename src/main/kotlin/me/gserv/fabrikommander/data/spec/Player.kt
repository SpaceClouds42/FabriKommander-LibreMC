package me.gserv.fabrikommander.data.spec

import kotlinx.serialization.Serializable
import me.gserv.fabrikommander.coolDown.CoolDowns
import java.util.*

@Serializable
data class Player(
    val name: String,

    val homes: MutableList<Home> = mutableListOf(),

    var backPos: Pos? = null,

    var rank: String = "member",

    var homeLimit: Int? = 3,

    var muted: Boolean = false,

    var inStaffChat: Boolean = false,

    var nick: String? = null,

    var coolDowns: MutableMap<CoolDowns, String> = EnumMap(CoolDowns::class.java),
)
