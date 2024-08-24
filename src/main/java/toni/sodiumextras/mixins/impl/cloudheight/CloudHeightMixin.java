package toni.sodiumextras.mixins.impl.cloudheight;

import toni.sodiumextras.EmbyConfig;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DimensionSpecialEffects.class)
public class CloudHeightMixin {
    @Shadow @Final private float cloudLevel;

    @Inject(method = "getCloudHeight", at = @At("HEAD"), cancellable = true)
    private void inject$cloudHeight(CallbackInfoReturnable<Float> cir) {
        if (cloudLevel == 192.0F)
            cir.setReturnValue((float) EmbyConfig.cloudsHeightCache);
    }
}