package me.gserv.fabrikommander.mixin.cauldrondupepatch;

import me.gserv.fabrikommander.cauldrondupepatch.DuperLocator;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow
    @Final
    private Block block;

    private ItemEntity lastItem;

    @Inject(method = "method_33261", at = @At("HEAD"), cancellable = true)
    private void dropItemContents(ItemEntity itemEntity, CallbackInfo ci) {
        if (this.block instanceof ShulkerBoxBlock) {
            if (lastItem != null && lastItem == itemEntity) {
                System.out.println("Dupe potentially attempted at " + (int) itemEntity.getX() + " " + (int) itemEntity.getY() + " " + (int) itemEntity.getZ() + " in world " + itemEntity.getEntityWorld().getRegistryKey().getValue() + " by player " + DuperLocator.getDuper(itemEntity));
                ci.cancel();
            }
            lastItem = itemEntity;
        }
    }
}
