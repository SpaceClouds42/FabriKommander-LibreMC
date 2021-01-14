package me.gserv.fabrikommander.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "hasGlint", at = @At("HEAD"), cancellable = true)
    public void hasGlint(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;
        if (itemStack.hasTag() && itemStack.getTag().contains("Glint") && itemStack.getTag().getBoolean("Glint")) {
            cir.setReturnValue(true);
        }
    }
}