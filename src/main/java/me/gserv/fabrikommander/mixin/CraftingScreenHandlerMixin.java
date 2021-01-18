package me.gserv.fabrikommander.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {
    public CraftingScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Shadow
    @Final
    private ScreenHandlerContext context;

    @Shadow
    protected static void updateResult(
            int syncId,
            World world,
            PlayerEntity player,
            CraftingInventory craftingInventory,
            CraftingResultInventory resultInventory
    ) {}

    @Shadow
    @Final
    private PlayerEntity player;

    @Shadow
    @Final
    private CraftingInventory input;

    @Shadow
    @Final
    private CraftingResultInventory result;

    private final ScreenHandlerContext empty = ScreenHandlerContext.EMPTY;

    @Inject(method = "onContentChanged", at = @At("HEAD"), cancellable = true)
    private void onContentChanged(Inventory inventory, CallbackInfo ci) {
        if (context == empty) {
            updateResult(this.syncId, this.player.getEntityWorld(), this.player, this.input, this.result);
            ci.cancel();
        }
    }

    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    private void close(PlayerEntity player, CallbackInfo ci) {
        if (context == empty) {
            super.close(player);
            this.dropInventory(player, this.input);
            ci.cancel();
        }
    }

}
