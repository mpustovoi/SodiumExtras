package toni.sodiumextras.mixins.impl.embeddium;

import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toni.sodiumextras.foundation.embeddium.pages.EntityCullingPage;
import toni.sodiumextras.foundation.embeddium.pages.OthersPage;
import toni.sodiumextras.foundation.embeddium.pages.TrueDarknessPage;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class, remap = false, priority = 100/* Prevents other forks of sodium extra stay above emb++*/)
public class EmbOptionsMixin {
    @Shadow @Final private List<OptionPage> pages;
//
//    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
//    private void redirect$qualityPage(Screen prevScreen, CallbackInfo ci) {
//        pages.add(new QualityPlusPage());
//    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$dynLightsPage(Screen prevScreen, CallbackInfo ci) {
        //pages.add(new QualityPlusPage());
        pages.add(new TrueDarknessPage());
        pages.add(new EntityCullingPage());
        pages.add(new OthersPage());
    }
}