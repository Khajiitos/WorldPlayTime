package me.khajiitos.worldplaytime.common.mixin;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.DataFixer;
import me.khajiitos.worldplaytime.common.IWithPlayTime;
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

@Mixin(LevelStorageSource.class)
public class LevelStorageSourceMixin {

    // Mixin doesn't particularly like remapping lambdas, so we add the lambda's obfuscated name as an alias
    @Inject(at = @At("RETURN"), method = {"lambda$levelSummaryReader$5", "method_29015"}, remap = false)
    public void onReturnLevelSummary(LevelStorageSource.LevelDirectory levelDirectory, boolean idk, Path path, DataFixer dataFixer, CallbackInfoReturnable<LevelSummary> cir) {
        LevelSummary levelSummary = cir.getReturnValue();

        if (levelSummary instanceof IWithPlayTime withPlayTime) {
            Path stats = path.getParent().resolve("stats");
            File statsFile = stats.toFile();

            if (statsFile.isDirectory()) {
               File[] saveFiles = statsFile.listFiles();

                if (saveFiles != null) {
                    int totalPlayTime = 0;
                    for (File file : saveFiles) {
                        // file should be a .json file containing stats

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
                        } catch (JsonIOException | IOException | IllegalStateException ignored) {}
                    }

                    if (totalPlayTime > 0) {
                        withPlayTime.setPlayTimeTicks(totalPlayTime);
                    }
                }
            }
        }
    }
}
