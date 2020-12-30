package me.gserv.fabrikommander.data.spec

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,

    val homes: MutableList<Home> = mutableListOf(),

    var homeLimit: Int? = null,

    var backPos: Pos? = null,

    var rank: String = "member"
)
