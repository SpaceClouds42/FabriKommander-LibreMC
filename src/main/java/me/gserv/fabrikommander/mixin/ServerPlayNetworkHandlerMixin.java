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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gserv.fabrikommander.utils.LoggersKt.log;
import static me.gserv.fabrikommander.utils.RanksKt.*;
import static me.gserv.fabrikommander.utils.TextFormatterKt.formatChatMessage;
import static me.gserv.fabrikommander.utils.TextKt.*;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
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
            int spaceAfterTargetPlayer = 5 + packet.getChatMessage().substring(5).indexOf(" ");
            String message = packet.getChatMessage().substring(spaceAfterTargetPlayer);
            String targetPlayer = packet.getChatMessage().substring(5, spaceAfterTargetPlayer);
            log("chat.private", player.getEntityName() + " to " + targetPlayer + ":" + message);
        } else if (packet.getChatMessage().startsWith("/tell")) {
            int spaceAfterTargetPlayer = 6 + packet.getChatMessage().substring(6).indexOf(" ");
            String message = packet.getChatMessage().substring(spaceAfterTargetPlayer);
            String targetPlayer = packet.getChatMessage().substring(6, spaceAfterTargetPlayer);
            log("chat.private", player.getEntityName() + " to " + targetPlayer + ":" + message);
        } else if (packet.getChatMessage().startsWith("/w")) {
            int spaceAfterTargetPlayer = 3 + packet.getChatMessage().substring(3).indexOf(" ");
            String message = packet.getChatMessage().substring(spaceAfterTargetPlayer);
            String targetPlayer = packet.getChatMessage().substring(3, spaceAfterTargetPlayer);
            log("chat.private", "[PM] " + player.getEntityName() + " to " + targetPlayer + ":" + message);
        }
    }

    private void broadcastChatMsg(MutableText message, String sender, String rawMessage) {
        server.getPlayerManager().broadcastChatMessage(
                message,
                MessageType.SYSTEM,
                Util.NIL_UUID
        );
        log("chat.main", "[Chat] " + sender + " > " + rawMessage);
    }

    private void broadcastStaffMsg(MutableText message, MinecraftServer server, String rawMessage, String sender) {
        server.getPlayerManager().getPlayerList().forEach(p -> {
            if (getStaffRanks().contains(PlayerDataManager.INSTANCE.getRank(p.getUuid()))) {
                p.sendSystemMessage(message, Util.NIL_UUID);
            }
        });
        log("chat.staff", "[Staff] " + sender + " > " + rawMessage);
    }
}
