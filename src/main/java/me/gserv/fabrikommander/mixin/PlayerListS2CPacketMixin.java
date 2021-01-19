package me.gserv.fabrikommander.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.gserv.fabrikommander.ConstantsKt.SERVER;
import static me.gserv.fabrikommander.extension.ServerPlayerEntityKt.nickName;
import static me.gserv.fabrikommander.utils.TextKt.*;

@Mixin(PlayerListS2CPacket.Entry.class)
public abstract class PlayerListS2CPacketMixin {
    @Shadow
    @Final
    private GameProfile profile;

    @Shadow
    @Final
    private int latency;

    @Shadow
    @Final
    private GameMode gameMode;

    @Inject(method = "getGameMode", at = @At(value = "HEAD", target = "Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Entry;getGameMode()Lnet/minecraft/world/GameMode;"), cancellable = true)
    private void removeSpectatorFlag(CallbackInfoReturnable<GameMode> cir) {
        if (gameMode == GameMode.SPECTATOR) {
            cir.setReturnValue(GameMode.SURVIVAL);
        } else {
            cir.setReturnValue(gameMode);
        }
    }

    @Inject(method = "getDisplayName", at = @At(value = "HEAD", target = "Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Entry;getDisplayName()Lnet/minecraft/text/Text;"), cancellable = true)
    private void modifyDisplayName(CallbackInfoReturnable<Text> cir) {
        Text text = reset("").append(
                nickName(SERVER.getPlayerManager().getPlayer(profile.getId()))
        ).append(
                gray("      " + latency + "ms")
        );
        cir.setReturnValue(text);
    }
}
