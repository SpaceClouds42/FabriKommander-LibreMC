package me.gserv.fabrikommander.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static me.gserv.fabrikommander.extension.PlayerEntityKt.nickName;
import static me.gserv.fabrikommander.utils.RequestPlayerKt.requestPlayer;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin_OverwriteDisplayName {
    @Shadow @Final private GameProfile gameProfile;

    /**
     * @author
     */
    @Overwrite
    public Text getDisplayName() throws CommandSyntaxException {
        requestPlayer(this.gameProfile);
        return nickName((PlayerEntity) (Object) this);
    }
}
