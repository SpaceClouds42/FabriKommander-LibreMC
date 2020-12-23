package me.gserv.fabrikommander.mixin;

import me.gserv.fabrikommander.data.PlayerDataManager;
import me.gserv.fabrikommander.utils.*;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.File;
import java.io.FilenameFilter;

import static me.gserv.fabrikommander.utils.TextKt.red;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    private Boolean isNewPlayer(ServerPlayerEntity player) {
        String uuid = player.getUuidAsString();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".dat");
            }
        };
        File dataDir = player.server.getSavePath(WorldSavePath.ROOT).resolve("playerdata").toFile();
        File[] files = dataDir.listFiles(filter);
        for (File file : files) {
            if (file.getName().equals(uuid + ".dat")) { return false; }
        }

        return true;
    }

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PlayerDataManager.INSTANCE.playerJoined(player);
        if (isNewPlayer(player)) {
            player.sendSystemMessage(
                    red("welcome"),
                    Util.NIL_UUID
            );
        }
    }

    @Inject(at = @At("RETURN"), method = "remove")
    private void remove(ServerPlayerEntity player, CallbackInfo ci) {
        PlayerDataManager.INSTANCE.playerLeft(player);
    }
}
