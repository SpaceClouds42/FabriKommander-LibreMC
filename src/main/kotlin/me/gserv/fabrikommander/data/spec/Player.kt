package me.gserv.fabrikommander.data.spec

import kotlinx.serialization.Serializable
import net.minecraft.text.LiteralText

@Serializable
data class Player(
    val name: String,

    val homes: MutableList<Home> = mutableListOf(),

    var backPos: Pos? = null,

    var rank: String = "member",

    var homeLimit: Int? = 3,

    var muted: Boolean = false,

    var inStaffChat: Boolean = false,

    var nick: String? = null
)
