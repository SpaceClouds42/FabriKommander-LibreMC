package me.gserv.fabrikommander.data.spec

import kotlinx.serialization.Serializable

@Serializable
class Warps (
    val warps: MutableList<Warp> = mutableListOf()
)