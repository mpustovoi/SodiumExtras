package toni.sodiumextras.mixins.impl.embeddium;

import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.Option;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import toni.sodiumextras.foundation.embeddium.EmbPlusOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class EmbPagesMixin {
    @Shadow @Final private static MinecraftOptionsStorage vanillaOpts;
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    @Redirect(method = "general", at = @At(
            value = "INVOKE",
            target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;",
            ordinal = 4))
    private static OptionGroup.Builder redirectFullScreenOption(OptionGroup.Builder instance, Option<?> option) {
        instance.add(EmbPlusOptions.getFullscreenOption(vanillaOpts));
        return instance;
    }

    @Inject(method = "quality", remap = true, at = @At(value = "NEW", target = "(Lnet/minecraft/network/chat/Component;Lcom/google/common/collect/ImmutableList;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionPage;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injectQualityOption(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        EmbPlusOptions.setQualityPlusOptions(groups, sodiumOpts);
    }

    @Inject(method = "general", at = @At(value = "NEW", target = "(Lnet/minecraft/network/chat/Component;Lcom/google/common/collect/ImmutableList;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionPage;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injectFPSOption(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        EmbPlusOptions.setFPSOptions(groups, sodiumOpts);
    }

    @Inject(method = "performance", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/services/PlatformRuntimeInformation;isDevelopmentEnvironment()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void setPerformanceOptions(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        EmbPlusOptions.setPerformanceOptions(groups, sodiumOpts);
    }
}