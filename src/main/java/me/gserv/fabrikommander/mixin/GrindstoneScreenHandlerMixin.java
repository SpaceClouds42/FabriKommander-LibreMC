package me.gserv.fabrikommander.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin {
    @Inject(method = "grind", at = @At("RETURN"), cancellable = true)
    private void reApplyGlint(ItemStack item, int damage, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (item.hasTag() && item.getTag().contains("Glint") && item.getTag().getBoolean("Glint")) {
            if (!item.hasEnchantments()) {
                item.addEnchantment(null, 0);
            }
            item.removeSubTag("RepairCost");
            cir.setReturnValue(item);
        }
    }
}
