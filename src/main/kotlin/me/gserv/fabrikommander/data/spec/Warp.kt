package me.gserv.fabrikommander.data.spec

import kotlinx.serialization.Serializable

@Serializable
data class Warp (
    val name: String,

    val pos: Pos,

    val createdBy: String
)