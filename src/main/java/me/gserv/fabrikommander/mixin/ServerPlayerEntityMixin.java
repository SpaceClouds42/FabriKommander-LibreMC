package me.gserv.fabrikommander.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import static me.gserv.fabrikommander.extension.ServerPlayerEntityKt.nickName;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    public Text getDisplayName() {
        return nickName((ServerPlayerEntity) (Object) this);
    }
}
