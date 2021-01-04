package me.gserv.fabrikommander.mixin;

import me.gserv.fabrikommander.data.PlayerDataManager;
import me.gserv.fabrikommander.data.SpawnDataManager;
import me.gserv.fabrikommander.data.spec.Spawn;
import static me.gserv.fabrikommander.utils.TextKt.*;
import static me.gserv.fabrikommander.utils.TextFormatterKt.*;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import java.io.File;
import java.io.FilenameFilter;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
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

        // Welcome new player and teleport them to spawn
        if (isNewPlayer(player)) {
            Spawn spawn = SpawnDataManager.INSTANCE.getSpawn();
            ServerWorld spawnWorld = player.server.getWorld(RegistryKey.of(Registry.DIMENSION, spawn.getPos().getWorld()));
            MutableText welcomeMessage = blue("Welcome ").append(
                    gray((MutableText) player.getDisplayName())
            ).append(
                    blue(" to ")
            ).append(
                    bold(aqua("LibreMC"))
            );
            PlayerLookup.all(player.getServer()).forEach(p->p.sendSystemMessage(welcomeMessage, Util.NIL_UUID));
            player.sendSystemMessage(
                    blue("Be sure to check the rules and info"),
                    Util.NIL_UUID
            );
            player.teleport(
                    spawnWorld,
                    spawn.getPos().getX(),
                    spawn.getPos().getY(),
                    spawn.getPos().getZ(),

                    spawn.getPos().getYaw(),
                    spawn.getPos().getPitch()
            );
        }
    }

    @Inject(at = @At("RETURN"), method = "remove")
    private void remove(ServerPlayerEntity player, CallbackInfo ci) {
        PlayerDataManager.INSTANCE.playerLeft(player);
    }

    @Shadow
    public abstract void sendToAll(Packet<?> packet);

    @Inject(at= @At("HEAD"), method = "updatePlayerLatency")
    public void updatePlayerLatency(CallbackInfo ci) {
        @SuppressWarnings("ConstantConditions")
        PlayerListMixin packet = (PlayerListMixin) new PlayerListHeaderS2CPacket();
        packet.setFooter(new LiteralText(formatString("&a&lTPS: #TPS#NMSPT: #MSPT")));
        packet.setHeader(new LiteralText(formatString("&b&lLibreMC#N&7Uptime: #UPTIME")));
        this.sendToAll(packet);
    }
}
