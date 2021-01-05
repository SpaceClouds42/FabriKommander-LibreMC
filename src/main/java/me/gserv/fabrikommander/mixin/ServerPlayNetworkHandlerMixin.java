package me.gserv.fabrikommander.mixin;

import me.gserv.fabrikommander.data.PlayerDataManager;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gserv.fabrikommander.utils.RanksKt.*;
import static me.gserv.fabrikommander.utils.TextFormatterKt.formatChatMessage;
import static me.gserv.fabrikommander.utils.TextKt.*;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    private final Logger CHAT_LOGGER = (Logger) LogManager.getLogger("Chat");
    private final Logger STAFF_CHAT_LOGGER = (Logger) LogManager.getLogger("Staff Chat");
    private final Logger PRIVATE_CHAT_LOGGER = (Logger) LogManager.getLogger("Private Message");

    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    private MinecraftServer server;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void broadcastChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        if (PlayerDataManager.INSTANCE.isInStaffChat(player.getUuid()) && !packet.getChatMessage().startsWith("/")) {
            String message = packet.getChatMessage();
            broadcastStaffMsg(
                    gray("[").append(
                            yellow("Staff")
                    ).append(
                            gray("] ")
                    ).append(
                            gray(player.getEntityName())
                    ).append(
                            darkGray(bold(" > "))
                    ).append(
                            new LiteralText(message)
                    ),
                    server,
                    message,
                    player.getEntityName()
            );
            info.cancel();
        } else if (!packet.getChatMessage().startsWith("/")) {
            String rawMessage = packet.getChatMessage();

            broadcastChatMsg(
                    formatChatMessage(player, rawMessage),
                    player.getEntityName(),
                    rawMessage
            );
            info.cancel();
        } else if (packet.getChatMessage().startsWith("/msg")) {
            int spaceAfterTargetPlayer = packet.getChatMessage().substring(4).indexOf(" ");
            String message = packet.getChatMessage().substring(spaceAfterTargetPlayer);
            String targetPlayer = packet.getChatMessage().substring(5, spaceAfterTargetPlayer);
            PRIVATE_CHAT_LOGGER.info(player.getEntityName() + " to " + targetPlayer + ": " + message);
        }
    }

    private void broadcastChatMsg(MutableText message, String sender, String rawMessage) {
        server.getPlayerManager().broadcastChatMessage(
                message,
                MessageType.SYSTEM,
                Util.NIL_UUID
        );
        CHAT_LOGGER.info("[Chat] " + sender + " > " + rawMessage);
    }

    private void broadcastStaffMsg(MutableText message, MinecraftServer server, String rawMessage, String sender) {
        server.getPlayerManager().getPlayerList().forEach(p -> {
            if (getStaffRanks().contains(PlayerDataManager.INSTANCE.getRank(p.getUuid()))) {
                p.sendSystemMessage(message, Util.NIL_UUID);
            }
        });
        STAFF_CHAT_LOGGER.info("[Staff] " + sender + " > " + rawMessage);
    }
}
