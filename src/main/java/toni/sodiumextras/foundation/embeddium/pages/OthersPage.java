package toni.sodiumextras.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import toni.sodiumextras.EmbyConfig;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

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
                        .setBinding((options, value) -> EmbyConfig.borderlessAttachModeF11.set(value),
                                (options) -> EmbyConfig.borderlessAttachModeF11.get())
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, mixinsOptionsStorage)
                        .setName(Component.translatable("sodium.extras.options.others.languagescreen.fastreload.title"))
                        .setTooltip(Component.translatable("sodium.extras.options.others.languagescreen.fastreload.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> {
                                    EmbyConfig.fastLanguageReload.set(value);
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
