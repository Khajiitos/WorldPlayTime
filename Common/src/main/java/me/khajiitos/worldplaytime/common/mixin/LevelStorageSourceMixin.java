package me.khajiitos.worldplaytime.common.mixin;

import com.google.gson.*;
import com.mojang.serialization.Dynamic;
import me.khajiitos.worldplaytime.common.util.IWithPlayTime;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Mixin(LevelStorageSource.class)
public class LevelStorageSourceMixin {

    @Inject(at = @At("RETURN"), method = "makeLevelSummary", remap = false)
    public void onMakeLevelSummary(Dynamic<?> dynamic, LevelStorageSource.LevelDirectory levelDirectory, boolean locked, int version, CallbackInfoReturnable<LevelSummary> cir) {
        LevelSummary levelSummary = cir.getReturnValue();

        if (levelSummary instanceof IWithPlayTime withPlayTime) {
            Path stats = levelDirectory.resourcePath(LevelResource.PLAYER_STATS_DIR);
            File statsFile = stats.toFile();

            if (statsFile.isDirectory()) {
               File[] saveFiles = statsFile.listFiles();

                if (saveFiles != null) {
                    int totalPlayTime = 0;
                    for (File file : saveFiles) {
                        // file should be a .json file containing stats
                        // Also make sure it has the [UUID].json filename

                        if (!file.getName().endsWith(".json")) {
                            continue;
                        }

                        try {
                            UUID.fromString(file.getName().substring(0, file.getName().length() - 5));
                        } catch (IllegalArgumentException e) {
                            continue;
                        }

                        try (FileReader fileReader = new FileReader(file)) {
                            JsonObject jsonObject =  JsonParser.parseReader(fileReader).getAsJsonObject();

                            if (jsonObject.has("stats")) {
                                JsonObject statsObject = jsonObject.getAsJsonObject("stats");

                                if (statsObject.has("minecraft:custom")) {
                                    JsonObject customObject = statsObject.getAsJsonObject("minecraft:custom");

                                    if (customObject.has("minecraft:play_time")) {
                                        int playTime = customObject.get("minecraft:play_time").getAsInt();
                                        totalPlayTime += playTime;
                                    }
                                }
                            }
                        } catch (JsonParseException | ClassCastException | IOException | IllegalStateException ignored) {}
                    }

                    if (totalPlayTime > 0) {
                        withPlayTime.worldPlayTime$setPlayTimeTicks(totalPlayTime);
                    }
                }
            }
        }
    }
}
