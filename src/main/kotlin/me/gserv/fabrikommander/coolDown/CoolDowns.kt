package me.gserv.fabrikommander.coolDown

//TODO: ? Consider changing constructor to require cooldown duration and remove cooldown objects
//TODO: cooldown cancel
enum class CoolDowns(val coolDownObject: CoolDown) {
    GLINT(GlintCoolDown()),
    RTP(RtpCoolDown()),
}