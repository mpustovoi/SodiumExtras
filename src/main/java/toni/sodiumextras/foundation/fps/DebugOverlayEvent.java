package toni.sodiumextras.foundation.fps;


import toni.sodiumextras.EmbyConfig;
import toni.sodiumextras.EmbyTools;
import toni.sodiumextras.SodiumExtras;
import toni.sodiumextras.foundation.fps.accessors.IUsageGPU;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
#if AFTER_21_1 import net.minecraft.client.DeltaTracker; #endif

#if NEO
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
#endif

#if FORGE
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
#endif

#if NEO
@EventBusSubscriber(modid = SodiumExtras.ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
#elif FORGE
@EventBusSubscriber(modid = SodiumExtras.ID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
#endif
public class DebugOverlayEvent {
    private static final FPSDisplay DISPLAY = new FPSDisplay();

    private static final Component MSG_FPS = Component.translatable("sodium.extras.options.displayfps.fps");
    private static final Component MSG_MIN = Component.translatable("sodium.extras.options.displayfps.min");
    private static final Component MSG_AVG = Component.translatable("sodium.extras.options.displayfps.avg");
    private static final Component MSG_GPU = Component.translatable("sodium.extras.options.displayfps.gpu");
    private static final Component MSG_MEM = Component.translatable("sodium.extras.options.displayfps.mem");

    private static final AverageQueue AVERAGE = new AverageQueue();

    private static int fps = -1;
    private static int minFPS = -1;
    private static int lastAvgFps = -1; // NEEDED
    private static int avgFPS = -1;
    private static int gpuPercent = -1;
    private static int memUsage = -1;

    #if FORGELIKE
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        var minecraft = Minecraft.getInstance();

        int currentFps = minecraft.getFps();
        SodiumExtras.fpsHistory.add(currentFps);
        renderFPSChar(minecraft, event.getGuiGraphics());
    }
    #endif

    public static void renderFPSChar(Minecraft mc, GuiGraphics graphics) {
        #if AFTER_21_1
        if (mc.getDebugOverlay().showDebugScreen()) return; // No render when F3 is open
        #else
        if (mc.options.renderDebug || mc.options.renderFpsChart) return; // No render when F3 is open
        #endif

        final var font = mc.font;
        final var scale = mc.options.guiScale().get();
        final var mode = EmbyConfig.fpsDisplayMode.get();
        final var systemMode = EmbyConfig.fpsDisplaySystemMode.get();

        if (mode.off() && systemMode.off()) return; // NOTHING TO DO HERE, BACK TO WORK

        DISPLAY.release();

        // FPS
        switch (mode) {
            case SIMPLE -> DISPLAY.append(calculateFPS$getColor(mc)).add(fix(fps)).add(" ").add(MSG_FPS.getString()).add(ChatFormatting.RESET);
            case ADVANCED -> {
                DISPLAY.append(calculateFPS$getColor(mc)).add(fix(fps)).add(ChatFormatting.RESET);
                DISPLAY.append(calculateMinFPS$getColor(mc)).add(MSG_MIN).add(" ").add(fix(minFPS)).add(ChatFormatting.RESET);
                DISPLAY.append(calculateAvgFPS$getColor(mc)).add(MSG_AVG).add(" ").add(fix(avgFPS)).add(ChatFormatting.RESET);
            }
        }
        if (!DISPLAY.isEmpty()) DISPLAY.split();

        // GPU AND RAM
        switch (systemMode) {
            case GPU ->
                    DISPLAY.append(calculateGPUPercent$getColor(mc)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
            case RAM ->
                    DISPLAY.append(calculateMemPercent$getColor()).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            case ON -> {
                DISPLAY.append(calculateGPUPercent$getColor(mc)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
                DISPLAY.append(calculateMemPercent$getColor()).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            }
        }

        if (DISPLAY.isEmpty()) DISPLAY.add("FATAL ERROR");

        float margin = (scale > 0) ? EmbyConfig.fpsDisplayMarginCache / (float) scale : EmbyConfig.fpsDisplayMarginCache;

        // Prevent FPS-Display to render outside screenspace
        String displayString = DISPLAY.toString();
        float maxPosX = graphics.guiWidth() - font.width(displayString);
        float posX, posY;

        posX = switch (EmbyConfig.fpsDisplayGravity.get()) {
            case LEFT -> margin;
            case CENTER -> (maxPosX / 2);
            case RIGHT -> maxPosX - margin;
        };
        posY = margin;

        graphics.pose().pushPose();
        if (EmbyConfig.fpsDisplayShadowCache) {
            graphics.fill((int) posX - 2, (int) posY - 2, (int) posX + font.width(displayString) + 2, (int) (posY + font.lineHeight) + 1, -1873784752);
            graphics.flush();
        }

        graphics.drawString(font, displayString, (int) posX, (int) posY, 0xffffffff, true);
        DISPLAY.release();
        graphics.pose().popPose();
    }

    private static ChatFormatting calculateFPS$getColor(Minecraft mc) {
        fps = mc.getFps();
        return EmbyTools.colorByLow(fps);
    }

    private static ChatFormatting calculateMinFPS$getColor(Minecraft mc) {
        //var timer = mc.getDebugOverlay().getTickTimeLogger();

        var history = SodiumExtras.fpsHistory;
        var min = history.getMinimum();
//        var min = history.get();
//
//        int start = timer.getLogStart();
//        int end = timer.getLogEnd();
//
//        if (end == start) return EmbyTools.colorByLow(minFPS);
//
//        int fps = mc.getFps();
//        if (fps <= 0) fps = 1;
//
//        long[] frames = timer.getLog();
//        long maxNS = (long) (1 / (double) fps * 1000000000);
//        long totalNS = 0;
//
//        int index = Math.floorMod(end - 1, frames.length);
//        while (index != start && (double) totalNS < 1000000000) {
//            long timeNs = frames[index];
//            if (timeNs > maxNS) {
//                maxNS = timeNs;
//            }
//
//            totalNS += timeNs;
//            index = Math.floorMod(index - 1, frames.length);
//        }

        minFPS = min;//(int) (1 / ((double) maxNS / 1000000000));
        return EmbyTools.colorByLow(minFPS);
    }

    private static ChatFormatting calculateAvgFPS$getColor(Minecraft mc) {
        if (mc.getFps() != lastAvgFps) { // DON'T BLOOD AVG
            AVERAGE.push(lastAvgFps = mc.getFps());
            avgFPS = AVERAGE.calculate();
        }
        return EmbyTools.colorByLow(avgFPS);
    }

    private static ChatFormatting calculateGPUPercent$getColor(Minecraft mc) {
        int value = (int) ((IUsageGPU) mc).embPlus$getSyncGpu();
        gpuPercent = (value > 0) ? Math.min(value, 100) : -1;
        return EmbyTools.colorByPercent(gpuPercent);
    }

    private static ChatFormatting calculateMemPercent$getColor() {
        memUsage = (int) ((EmbyTools.ramUsed() * 100) / Runtime.getRuntime().maxMemory());
        return EmbyTools.colorByPercent(memUsage);
    }

    private static String fix(int value) {
        return (value == -1) ? "--" : "" + value;
    }

    public static class AverageQueue {
        private final int[] QUEUE = new int[14];
        private int used = 0;

        void push(int value) {
            if (used == QUEUE.length) used = 0;
            QUEUE[used] = value;
            used++;
        }

        int calculate() {
            int times = 0;
            for (int i = 0; i < used; i++) {
                times += QUEUE[i];
            }

            return times / used;
        }
    }
}