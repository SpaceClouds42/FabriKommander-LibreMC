package me.gserv.fabrikommander.mixin;

import me.gserv.fabrikommander.data.PlayerDataManager;
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

import static me.gserv.fabrikommander.utils.TextKt.*;
import static me.gserv.fabrikommander.utils.RanksKt.getStaffRanks;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    private MinecraftServer server;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void broadcastChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        /* if (MuteCommand.isMuted(player.getUuidAsString())&&!Utils.hasPermission(player, "mute")) {
            player.sendSystemMessage(new LiteralText("You were muted! Could not send message, contact a moderator if you feel this is a mistake"), Util.NIL_UUID);
            info.cancel();
        }
        else */ if (PlayerDataManager.INSTANCE.isInStaffChat(player.getUuid()) && !packet.getChatMessage().startsWith("/")) {
            String message = packet.getChatMessage();
            sendToStaffChat(
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
                    server
            );
            info.cancel();
        }
        /* else if(ServerMuteCommand.isMuted()&&!Utils.hasPermission(player, "servermute")){
            String serverMuted = "The server has been muted, please contact the moderators to unmute";
            Text serverIsMuted = new LiteralText(String.format("%s %s", "§4", serverMuted));
            player.sendSystemMessage(serverIsMuted, Util.NIL_UUID);
            info.cancel();
        } */
    }

    private void sendToStaffChat(MutableText message, MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(p -> {
            if (getStaffRanks().contains(PlayerDataManager.INSTANCE.getRank(p.getUuid()))) {
                p.sendSystemMessage(message, Util.NIL_UUID);
            }
        });
        System.out.println(message.toString());
    }
}
