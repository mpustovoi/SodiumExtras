package toni.sodiumextras;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import toni.sodiumextras.foundation.fps.DebugOverlayEvent;
import toni.sodiumextras.foundation.fps.FpsHistory;

#if FABRIC
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
#endif

#if NEO

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(SodiumExtras.ID)
#endif
public class SodiumExtras #if FABRIC implements ClientModInitializer #endif {
    public static final String ID = "sodiumextras";
    public static final Logger LOGGER = LogManager.getLogger("SodiumExtras");

    public static FpsHistory fpsHistory = new FpsHistory();

    public SodiumExtras(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
        #if NEO
        modContainer.registerConfig(ModConfig.Type.CLIENT, EmbyConfig.SPECS);
        #endif
    }

    #if FABRIC
    @Override
    public void onInitializeClient() {
        NeoForgeModConfigEvents.loading(SodiumExtras.ID).register((ModConfig config) -> {
            EmbyConfig.updateCache();
        });

        NeoForgeConfigRegistry.INSTANCE.register(SodiumExtras.ID, ModConfig.Type.CLIENT, EmbyConfig.SPECS);

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            DebugOverlayEvent.renderFPSChar(Minecraft.getInstance(), drawContext);
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            int currentFps = client.getFps();
            fpsHistory.add(currentFps);
        });
    }
    #endif
}