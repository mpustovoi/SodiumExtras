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
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
#else
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
#endif

public class OthersPage extends OptionPage {
    private static final SodiumOptionsStorage mixinsOptionsStorage = new SodiumOptionsStorage();

    public OthersPage() {
        super(Component.translatable("sodium.extras.options.others.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(EmbyConfig.AttachMode.class, mixinsOptionsStorage)
                        .setName(Component.translatable("sodium.extras.options.others.borderless.attachmode.title"))
                        .setTooltip(Component.translatable("sodium.extras.options.others.borderless.attachmode.desc"))
                        .setControl(option -> new CyclingControl<>(option, EmbyConfig.AttachMode.class, new Component[] {
                                Component.translatable("sodium.extras.options.common.attach"),
                                Component.translatable("sodium.extras.options.common.replace"),
                                Component.translatable("sodium.extras.options.common.off")
                        }))
                        .setBinding((options, value) -> {
                                    EmbyConfig.borderlessAttachModeF11.set(value);
                                    EmbyConfig.SPECS.save();
                                },
                                (options) -> EmbyConfig.borderlessAttachModeF11.get())
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, mixinsOptionsStorage)
                        .setName(Component.translatable("sodium.extras.options.others.languagescreen.fastreload.title"))
                        .setTooltip(Component.translatable("sodium.extras.options.others.languagescreen.fastreload.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> {
                                    EmbyConfig.fastLanguageReload.set(value);
                                    EmbyConfig.SPECS.save();
                                    EmbyConfig.fastLanguageReloadCache = value;
                                },
                                (options) -> EmbyConfig.fastLanguageReloadCache)
                        .build()
                )
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
