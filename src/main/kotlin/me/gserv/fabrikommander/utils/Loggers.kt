package me.gserv.fabrikommander.utils

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


val CHAT_LOGGER = LogManager.getLogger("Chat") as Logger
val JOIN_CHAT_LOGGER = LogManager.getLogger("Join") as Logger
val LEAVE_CHAT_LOGGER = LogManager.getLogger("Leave") as Logger
val STAFF_CHAT_LOGGER = LogManager.getLogger("Staff Chat") as Logger
val PRIVATE_CHAT_LOGGER = LogManager.getLogger("Private Message") as Logger
val RANK_LOGGER = LogManager.getLogger("Rank Manager") as Logger

val loggers = hashMapOf(
    "chat.main" to CHAT_LOGGER,
    "chat.join" to JOIN_CHAT_LOGGER,
    "chat.leave" to LEAVE_CHAT_LOGGER,
    "chat.staff" to STAFF_CHAT_LOGGER,
    "chat.private" to PRIVATE_CHAT_LOGGER,
    "rank.main" to RANK_LOGGER
)

fun log(logger: String, message: String) {
    loggers[logger]?.info(message)
}