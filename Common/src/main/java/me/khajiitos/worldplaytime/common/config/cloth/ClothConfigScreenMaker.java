package me.khajiitos.worldplaytime.common.config.cloth;

import me.khajiitos.worldplaytime.common.config.WPTConfig;
import me.khajiitos.worldplaytime.common.util.Color;
import me.khajiitos.worldplaytime.common.util.ServerEntryRenderPos;
import me.khajiitos.worldplaytime.common.util.WorldEntryRenderPos;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class ClothConfigScreenMaker {

    public static Screen create(Minecraft minecraft, Screen parent) {
        return create(parent);
    }

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableComponent("worldplaytime.config.header"))
                .setSavingRunnable(WPTConfig::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory worldEntryCategory = builder.getOrCreateCategory(new TranslatableComponent("worldplaytime.config.category.world_entry"));
        ConfigCategory serverEntryCategory = builder.getOrCreateCategory(new TranslatableComponent("worldplaytime.config.category.server_entry"));

        worldEntryCategory.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("worldplaytime.config.showWorldPlayTime"), WPTConfig.showWorldPlayTime.get())
                .setDefaultValue(WPTConfig.showWorldPlayTime::getDefault)
                .setSaveConsumer(WPTConfig.showWorldPlayTime::set)
                .build());

        worldEntryCategory.addEntry(entryBuilder.startEnumSelector(new TranslatableComponent("worldplaytime.config.worldPlayTimePosition"), WorldEntryRenderPos.class, WPTConfig.worldPlayTimePosition.get())
                .setDefaultValue(WPTConfig.worldPlayTimePosition::getDefault)
                .setSaveConsumer(WPTConfig.worldPlayTimePosition::set)
                .setEnumNameProvider(anEnum -> new TranslatableComponent("worldplaytime.config.worldPlayTimePosition." + anEnum.name().toLowerCase()))
                .build());

        worldEntryCategory.addEntry(entryBuilder.startAlphaColorField(new TranslatableComponent("worldplaytime.config.worldPlayTimeColor"), WPTConfig.worldPlayTimeColor.get().toARGB())
                .setDefaultValue(WPTConfig.worldPlayTimeColor.getDefault()::toARGB)
                .setSaveConsumer(argb -> WPTConfig.worldPlayTimeColor.set(Color.fromARGB(argb)))
                .build());

        serverEntryCategory.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("worldplaytime.config.showServerPlayTime"), WPTConfig.showServerPlayTime.get())
                .setDefaultValue(WPTConfig.showServerPlayTime::getDefault)
                .setSaveConsumer(WPTConfig.showServerPlayTime::set)
                .build());

        serverEntryCategory.addEntry(entryBuilder.startEnumSelector(new TranslatableComponent("worldplaytime.config.serverPlayTimePosition"), ServerEntryRenderPos.class, WPTConfig.serverPlayTimePosition.get())
                .setDefaultValue(WPTConfig.serverPlayTimePosition::getDefault)
                .setSaveConsumer(WPTConfig.serverPlayTimePosition::set)
                .setEnumNameProvider(anEnum -> new TranslatableComponent("worldplaytime.config.serverPlayTimePosition." + anEnum.name().toLowerCase()))
                .build());

        serverEntryCategory.addEntry(entryBuilder.startAlphaColorField(new TranslatableComponent("worldplaytime.config.serverPlayTimeColor"), WPTConfig.serverPlayTimeColor.get().toARGB())
                .setDefaultValue(WPTConfig.serverPlayTimeColor.getDefault()::toARGB)
                .setSaveConsumer(argb -> WPTConfig.serverPlayTimeColor.set(Color.fromARGB(argb)))
                .build());

        return builder.build();
    }
}
