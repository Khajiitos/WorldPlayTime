package me.khajiitos.worldplaytime.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.khajiitos.worldplaytime.common.WorldPlayTime;
import me.khajiitos.worldplaytime.common.util.ServerEntryRenderPos;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public class WPTConfig {
    private static final File file = new File("config/worldplaytime/config.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Entry
    public static final WPTConfigValues.BooleanValue showServerPlayTime = new WPTConfigValues.BooleanValue(false);
    @Entry
    public static final WPTConfigValues.EnumValue<ServerEntryRenderPos> serverPlayTimePosition = new WPTConfigValues.EnumValue<>(ServerEntryRenderPos.AFTER_NAME);

    public static void init() {
        if (!file.exists()) {
            save();
        } else {
            load();
        }
    }

    public static void save() {
        if (!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
            WorldPlayTime.LOGGER.error("Failed to create config/worldplaytime/ directory");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            JsonObject jsonObject = new JsonObject();

            for (Field field : WPTConfig.class.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Entry.class)) {
                    continue;
                }

                Object object = field.get(null);

                if (!(object instanceof WPTConfigValues.Value<?> configValue)) {
                    continue;
                }

                jsonObject.add(field.getName(), configValue.write());
            }

            GSON.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            WorldPlayTime.LOGGER.error("Failed to save the World Play Time config", e);
        } catch (IllegalAccessException e) {
            WorldPlayTime.LOGGER.error("Error while saving the World Play Time config", e);
        }
    }

    public static void load() {
        if (!file.exists()) {
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            for (Field field : WPTConfig.class.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Entry.class)) {
                    continue;
                }

                String fieldName = field.getName();

                if (!jsonObject.has(fieldName)) {
                    continue;
                }

                Object object = field.get(null);

                if (!(object instanceof WPTConfigValues.Value<?> configValue)) {
                    continue;
                }

                JsonElement jsonElement = jsonObject.get(fieldName);
                configValue.setUnchecked(configValue.read(jsonElement));
            }
        } catch (IOException e) {
            WorldPlayTime.LOGGER.error("Failed to read the World Play Time config", e);
        } catch (IllegalAccessException e) {
            WorldPlayTime.LOGGER.error("Error while reading the World Play Time config", e);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface Entry { }
}
