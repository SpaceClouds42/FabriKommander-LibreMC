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
    "VIP" to reset("") + gray("[") + darkGreen("VIP") + gray("] ") + reset(""),
    "VIP+" to reset("") + gray("[") + darkGreen("VIP") + green("+") + gray("] ") + reset(""),
    "MVP" to reset("") + gray("[") + gold("MVP") + gray("] ") + reset(""),
    "MVP+" to reset("") + gray("[") + gold("MVP") + green("+") + gray("] ") + reset(""),
    "OGDON" to reset("") + gray("[") + red("OGDON") + gray("] ") + reset(""),
    "Builder" to reset("") + gray("[") + darkRed("Builder") + gray("] ") + reset(""),
    "Helper" to reset("") + gray("[") + green("Helper") + gray("] ") + reset(""),
    "Mod" to reset("") + gray("[") + aqua("Mod") + gray("] ") + reset(""),
    "Dev" to reset("") + gray("[") + darkPurple("Dev") + gray("] ") + reset(""),
    "Owner" to reset("") + gray("[") + blue("Owner") + gray("] ") + reset("")
)

val LOGGER = LogManager.getLogger("FabriKommander-LibreMC") as Logger

@Deprecated (
    "hasRankPermissionLevel is now an extension function of ServerPlayerEntity",
    ReplaceWith(
        "ServerPlayerEntity.hasRankPermissionLevel(rank: String)",
        "import me.gserv.fabrikommander.extension.hasRankPermissionLevel"
    )
)
fun hasRankPermissionLevel(player: ServerPlayerEntity, rank: String): Boolean {
    return player.hasRankPermissionLevel(rank)
}

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

