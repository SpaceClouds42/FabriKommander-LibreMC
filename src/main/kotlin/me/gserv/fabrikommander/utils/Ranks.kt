package me.gserv.fabrikommander.utils

import me.gserv.fabrikommander.data.PlayerDataManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText
import org.apache.logging.log4j.core.Logger
import org.apache.logging.log4j.LogManager
import me.gserv.fabrikommander.extension.hasRankPermissionLevel

val ranks = listOf(
    "member",
    "VIP",
    "VIP+",
    "MVP",
    "MVP+",
    "OGDON",
    "Builder",
    "Helper",
    "Mod",
    "Dev",
    "Owner"
)

val staffRanks = listOf(
    "Builder",
    "Helper",
    "Mod",
    "Dev",
    "Owner"
)

val rankToHomeLimit = hashMapOf(
    "member" to 3,
    "VIP" to 5,
    "VIP+" to 8,
    "MVP" to 12,
    "MVP+" to 20,
    "OGDON" to 20,
    "Builder" to 8,
    "Helper" to 8,
    "Mod" to 8,
    "Dev" to 8,
    "Owner" to 8
)

val rankToPrefix = hashMapOf(
    "member" to reset(""),
    "VIP" to reset("") + darkGray("[") + darkGreen("VIP") + darkGray("] ") + reset(""),
    "VIP+" to reset("") + darkGray("[") + darkGreen("VIP") + green("+") + darkGray("] ") + reset(""),
    "MVP" to reset("") + darkGray("[") + gold("MVP") + darkGray("] ") + reset(""),
    "MVP+" to reset("") + darkGray("[") + gold("MVP") + green("+") + darkGray("] ") + reset(""),
    "OGDON" to reset("") + darkGray("[") + red("OGDON") + darkGray("] ") + reset(""),
    "Builder" to reset("") + darkGray("[") + darkRed("Builder") + darkGray("] ") + reset(""),
    "Helper" to reset("") + darkGray("[") + green("Helper") + darkGray("] ") + reset(""),
    "Mod" to reset("") + darkGray("[") + aqua("Mod") + darkGray("] ") + reset(""),
    "Dev" to reset("") + darkGray("[") + darkPurple("Dev") + darkGray("] ") + reset(""),
    "Owner" to reset("") + darkGray("[") + blue("Owner") + darkGray("] ") + reset("")
)

val LOGGER = LogManager.getLogger("FabriKommander-LibreMC") as Logger

@Deprecated (
    "hasRankPermissionLevel is now an extension function of ServerPlayerEntity",
    ReplaceWith(
        "player.hasRankPermissionLevel(rank)",
        "import me.gserv.fabrikommander.extension.hasRankPermissionLevel"
    )
)
fun hasRankPermissionLevel(player: ServerPlayerEntity, rank: String): Boolean {
    return player.hasRankPermissionLevel(rank)
}

@Deprecated(
    "This was placed in the wrong package :facepalm:",
    ReplaceWith(
        "ServerPlayerEntity.nickName()",
        "import me.gserv.fabrikommander.extension.customName"
    )
)
fun ServerPlayerEntity.customName(): MutableText {
    val prefix = rankToPrefix[PlayerDataManager.getRank(this.uuid)]!!.shallowCopy()
    val name: MutableText
    val nick = PlayerDataManager.getNick(this.uuid)
    if (nick == null) {
        name = reset("") +
                hover(
                    click(
                        gray(this.entityName),
                        ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            "/tell ${this.entityName}"
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        white(this.entityName +
                                "\nType: Player\n" +
                                this.uuid
                        )
                    )
                ) +
                reset("")
    } else {
        name = reset("") +
                hover(
                    click(
                        reset(nick),
                        ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            "/tell ${this.entityName}"
                        )
                    ),
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        white(this.entityName +
                                "\nType: Player\n" +
                                this.uuid
                        )
                    )
                ) +
                reset("")
    }

    return reset("") + prefix + name + reset("")
}

