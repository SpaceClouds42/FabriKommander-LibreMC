package me.gserv.fabrikommander.coolDown

import java.time.Duration

class RtpCoolDown : CoolDown {
    override val type: CoolDowns = CoolDowns.RTP
    override val duration: Duration = Duration.ofMinutes(1)
}