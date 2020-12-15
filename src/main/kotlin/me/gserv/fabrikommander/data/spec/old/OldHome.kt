@file:UseSerializers(IdentifierSerializer::class)

package me.gserv.fabrikommander.data.spec.old

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import me.gserv.fabrikommander.utils.IdentifierSerializer
import net.minecraft.util.Identifier

@Serializable
data class OldHome(
    val name: String,
    val world: Identifier,

    val x: Double,
    val y: Double,
    val z: Double,

    val pitch: Float,
    val yaw: Float
)