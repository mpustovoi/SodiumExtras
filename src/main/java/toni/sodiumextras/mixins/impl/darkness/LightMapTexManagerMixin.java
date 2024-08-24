package toni.sodiumextras.mixins.impl.darkness;

import toni.sodiumextras.foundation.darkness.accessors.TextureAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class LightMapTexManagerMixin {
	@Shadow @Final private DynamicTexture lightTexture;

	@Inject(method = "<init>*", at = @At(value = "RETURN"))
	private void inject$afterInit(GameRenderer gameRenderer, Minecraft minecraftClient, CallbackInfo ci) {
		((TextureAccess) lightTexture).embPlus$enableUploadHook();
	}
}