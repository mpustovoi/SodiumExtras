package toni.sodiumextras.mixins.impl.darkness;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import toni.sodiumextras.foundation.darkness.DarknessPlus;
import toni.sodiumextras.mixins.impl.darkness.accessors.LightTextureAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow @Final public LightTexture lightTexture;
	@Shadow public abstract Minecraft getMinecraft();

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void inject$renderLevel(DeltaTracker deltaTracker, CallbackInfo ci) {
		final var mc = getMinecraft();
		final var lightTexAccessor = (LightTextureAccessor) lightTexture;

		if (lightTexAccessor.isDirty()) {
			mc.getProfiler().push("lightTex");
			DarknessPlus.updateLuminance(deltaTracker.getGameTimeDeltaTicks(), mc, (GameRenderer) (Object) this, lightTexAccessor.getFlicker());
			mc.getProfiler().pop();
		}
	}
}