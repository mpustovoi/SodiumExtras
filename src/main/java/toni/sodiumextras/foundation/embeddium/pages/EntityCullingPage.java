package toni.sodiumextras.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import toni.sodiumextras.EmbyConfig;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

#if AFTER_21_1
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
#else
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
#endif

public class EntityCullingPage extends OptionPage {
    private static final SodiumOptionsStorage performanceOptionsStorage = new SodiumOptionsStorage();

    public EntityCullingPage() {
        super(Component.translatable("sodium.extras.options.culling.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var enableDistanceChecks = OptionImpl.createBuilder(boolean.class, performanceOptionsStorage)
                .setName(Component.translatable("sodium.extras.options.culling.entity.title"))
                .setTooltip(Component.translatable("sodium.extras.options.culling.entity.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.entityDistanceCulling.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.entityDistanceCullingCache = value;
                        },
                        (options) -> EmbyConfig.entityDistanceCullingCache)
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistance = OptionImpl.createBuilder(int.class, performanceOptionsStorage)
                .setName(Component.translatable("sodium.extras.options.culling.entity.distance.horizontal.title"))
                .setTooltip(Component.translatable("sodium.extras.options.culling.entity.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> {
                            int result = value * value;
                            EmbyConfig.entityCullingDistanceX.set(result);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.entityCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.entityCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistanceVertical = OptionImpl.createBuilder(int.class, performanceOptionsStorage)
                .setName(Component.translatable("sodium.extras.options.culling.entity.distance.vertical.title"))
                .setTooltip(Component.translatable("sodium.extras.options.culling.entity.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.entityCullingDistanceY.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.entityCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.entityCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );


        var enableTileDistanceChecks = OptionImpl.createBuilder(boolean.class, performanceOptionsStorage)
                .setName(Component.translatable("sodium.extras.options.culling.tiles.title"))
                .setTooltip(Component.translatable("sodium.extras.options.culling.tiles.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.tileEntityDistanceCulling.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.tileEntityDistanceCullingCache = value;
                        },
                        (options) -> EmbyConfig.tileEntityDistanceCullingCache)
                .setImpact(OptionImpact.HIGH)
                .build();


        var maxTileEntityDistance = OptionImpl.createBuilder(int.class, performanceOptionsStorage)
                .setName(Component.translatable("sodium.extras.options.culling.tile.distance.horizontal.title"))
                .setTooltip(Component.translatable("sodium.extras.options.culling.tile.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.biomeBlend()))
                .setBinding((options, value) -> {
                            int result = value * value;
                            EmbyConfig.tileEntityCullingDistanceX.set(result);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.tileEntityCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.tileEntityCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxTileEntityDistanceVertical = OptionImpl.createBuilder(int.class, performanceOptionsStorage)
                .setName(Component.translatable("sodium.extras.options.culling.tile.distance.vertical.title"))
                .setTooltip(Component.translatable("sodium.extras.options.culling.tile.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((options, value) -> {
                            EmbyConfig.tileEntityCullingDistanceY.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.tileEntityCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.tileEntityCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(enableTileDistanceChecks)
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
