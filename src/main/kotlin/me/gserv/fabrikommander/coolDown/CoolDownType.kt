package me.gserv.fabrikommander.coolDown

import java.time.Duration

//TODO: cooldown cancel
enum class CoolDownType(val duration: Duration, val staffExempt: Boolean) {
    GLINT(Duration.ofDays(7), true),
    RTP(Duration.ofMinutes(1), false),
}