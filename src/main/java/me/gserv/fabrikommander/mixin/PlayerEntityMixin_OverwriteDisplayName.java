package me.gserv.fabrikommander.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static me.gserv.fabrikommander.extension.PlayerEntityKt.nickName;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin_OverwriteDisplayName {
    /**
     * @author
     */
    @Overwrite
    public Text getDisplayName() {
        return nickName((PlayerEntity) (Object) this);
    }
}
