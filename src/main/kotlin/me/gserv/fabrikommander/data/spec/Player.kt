package me.gserv.fabrikommander.data.spec

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,

    val homes: MutableList<Home> = mutableListOf(),

    //val homeLimit: Int = 3,

    var backPos: Pos? = null
)
