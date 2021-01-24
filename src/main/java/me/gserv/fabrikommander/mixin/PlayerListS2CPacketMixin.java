package me.gserv.fabrikommander.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.gserv.fabrikommander.ConstantsKt.SERVER;
import static me.gserv.fabrikommander.extension.ServerPlayerEntityKt.nickName;
import static me.gserv.fabrikommander.utils.TextKt.*;

@Mixin(PlayerListS2CPacket.class)
public abstract class PlayerListS2CPacketMixin {
    @Mixin(PlayerListS2CPacket.Entry.class)
    private abstract static class EntryMixin {
        @Shadow
        @Final
        private GameProfile profile;

        @Shadow
        @Final
        private int latency;

        @Inject(method = "getDisplayName", at = @At(value = "HEAD", target = "Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Entry;getDisplayName()Lnet/minecraft/text/Text;"), cancellable = true)
        private void modifyDisplayName(CallbackInfoReturnable<Text> cir) {
            if (SERVER.getPlayerManager().getPlayer(profile.getId()) != null) {
                Text text = reset("").append(
                        nickName(SERVER.getPlayerManager().getPlayer(profile.getId()))
                ).append(
                        gray(" " + latency + "ms")
                );
                cir.setReturnValue(text);
            }
        }
    }
}
