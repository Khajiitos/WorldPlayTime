package me.khajiitos.worldplaytime.common.config.cloth;

import me.khajiitos.worldplaytime.common.config.WPTConfig;
import me.khajiitos.worldplaytime.common.util.ServerEntryRenderPos;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigScreenMaker {

    public static Screen create(Minecraft minecraft, Screen parent) {
        return create(parent);
    }

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("worldplaytime.config.header"))
                .setSavingRunnable(WPTConfig::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("worldplaytime.config.general"));

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("worldplaytime.config.showServerPlayTime"), WPTConfig.showServerPlayTime.get())
                        .setDefaultValue(WPTConfig.showServerPlayTime::getDefault)
                        .setSaveConsumer(WPTConfig.showServerPlayTime::set)
                        .build());

        general.addEntry(entryBuilder.startEnumSelector(Component.translatable("worldplaytime.config.serverPlayTimePosition"), ServerEntryRenderPos.class, WPTConfig.serverPlayTimePosition.get())
                        .setDefaultValue(WPTConfig.serverPlayTimePosition::getDefault)
                        .setSaveConsumer(WPTConfig.serverPlayTimePosition::set)
                        .setEnumNameProvider(anEnum -> Component.translatable("worldplaytime.config.serverPlayTimePosition." + anEnum.name().toLowerCase()))
                        .build());

        return builder.build();
    }
}
