package me.gserv.fabrikommander.mixin;

import me.gserv.fabrikommander.utils.TextFormatterKt;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiteralText.class)
public abstract class LiteralTextMixin {

    @Mutable
    @Final
    @Shadow
    public String string;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void LiteralText(String string, CallbackInfo ci) {
        System.out.println("YO!!! I GOT A LITERAL TEXT!: " + this.string);
        this.string = TextFormatterKt.formatString(string);
        System.out.println("Pog i formatted it: " + this.string);
    }
}
