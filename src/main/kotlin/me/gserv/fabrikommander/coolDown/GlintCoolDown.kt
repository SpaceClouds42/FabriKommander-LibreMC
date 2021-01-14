package me.gserv.fabrikommander.coolDown

import java.time.Duration

class GlintCoolDown : CoolDown {
    override val type: CoolDowns = CoolDowns.GLINT
    override val duration: Duration = Duration.ofDays(7)
}