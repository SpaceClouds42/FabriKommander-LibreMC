package me.gserv.fabrikommander.utils

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import kotlin.random.Random.Default.nextInt

fun formatString(text: String): String {
    return removeSlurs(customVariables((minecraftFormatting(text))))
}

fun minecraftFormatting(text: String): String {
    return text
        .replace("""(?<!\\)&(?=([0123456789abcdefklmnor]))""".toRegex(), "§")
        .replace("""\\(?=(&))""".toRegex(), "")
}

fun customVariables(text: String): String {
    return text
        .replace("#TPS".toRegex(), "%.2f".format(TablistVariables.INSTANCE.getTPS()))
        .replace("#MSPT".toRegex(), "%.2f".format(TablistVariables.INSTANCE.mspt))
        .replace("#UPTIME".toRegex(), TablistVariables.INSTANCE.getUptime())
        .replace("#N".toRegex(), "\n")
}

val slurs = listOf(
    "nigger",
    "n1gger",
    "n1g",
    "n1qger",
    "n1gqer",
    "n1qqer",
    "n1gga",
    "nigga",
    "nig",
    "niglet",
    "nicker",
    "nikker",
    "nigqer",
    "niqger",
    "niqqer",
    "fag",
    "faggot",
    "fagg0t",
    "coon",
    "wetback",
    "dyke",
    "tranny",
    "beaner",
    "chink",
    "cracker",
    "gringo",
    "chink",
    "ch1nk",
    "niggerfaggot",
)

fun randomCensorString(length: Int): String {
    val characters = "%$#@!^&*?"
    var censorString = ""
    for (i in 0 until length) {
        censorString += characters[nextInt(characters.length)]
    }
    return censorString
}

fun removeSlurs(text: String): String {
    val words: MutableList<String> = text.split(" ") as MutableList<String>
    val editedWords = mutableListOf<String>()
    for (word in words) {
        val noFormatWord = word
            // Remove any Minecraft format codes
            .replace("""§[0123456789abcdefklmnor]""".toRegex(), "")
            // Remove any interfering punctuation
            .replace("""[/~!@#$%^&*()_+?><,.;:"'|\\]""".toRegex(), "")
            .toLowerCase()
        if (slurs.contains(noFormatWord)) {
            editedWords.add("§k" + randomCensorString(noFormatWord.length) + "§r")
        } else {
            editedWords.add(word)
        }
    }
    return editedWords.joinToString(" ")
}

fun formatChatMessage(player: ServerPlayerEntity, message: String): MutableText {
    val name = reset("") + player.displayName + reset("")
    val connector = reset(" ") + darkGray(bold(">")) + reset(" ")
    val messageAsMutableText = reset(white(message))
    return name + connector + messageAsMutableText
}