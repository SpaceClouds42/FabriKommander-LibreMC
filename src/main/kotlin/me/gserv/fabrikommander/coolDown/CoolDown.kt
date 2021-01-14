package me.gserv.fabrikommander.coolDown

import java.time.Duration

interface CoolDown {
    //TODO: ? property type must be removed, but how..
    val type: CoolDowns
    val duration: Duration
}