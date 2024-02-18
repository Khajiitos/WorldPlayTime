package me.khajiitos.worldplaytime.common.config;

import me.khajiitos.worldplaytime.common.WorldPlayTime;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServerPlayTimeManager {
    private static final File file = new File("data/servers_playtime.dat");
    private static final HashMap<String, Integer> serverPlayTimes = new HashMap<>();

    public static void save() {
        if (serverPlayTimes.isEmpty()) {
            return;
        }

        if (!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
            WorldPlayTime.LOGGER.error("Failed to create config/worldplaytime/ directory");
            return;
        }

        CompoundTag compoundTag = new CompoundTag();

        for (Map.Entry<String, Integer> entry : serverPlayTimes.entrySet()) {
            String serverIp = entry.getKey();
            int playTime = entry.getValue();

            compoundTag.putInt(serverIp, playTime);
        }

        try {
            NbtIo.write(compoundTag, file.toPath());
        } catch (IOException e) {
            WorldPlayTime.LOGGER.error("Failed to write to servers_playtime.dat");
        }
    }

    public static void load() {
        if (!file.exists()) {
            return;
        }

        try {
            CompoundTag compoundTag = NbtIo.read(file.toPath());

            if (compoundTag == null) {
                return;
            }

            for (String serverIp : compoundTag.getAllKeys()) {
                int playTime = compoundTag.getInt(serverIp);

                if (playTime > 0) {
                    serverPlayTimes.put(serverIp, playTime);
                }
            }
        } catch (IOException e) {
            WorldPlayTime.LOGGER.error("Failed to read servers_playtime.dat");
        }
    }

    public static void saveAsync() {
        CompletableFuture.runAsync(ServerPlayTimeManager::save);
    }

    public static void onPlayTick(String serverIp) {
        serverPlayTimes.put(serverIp, serverPlayTimes.getOrDefault(serverIp, 0) + 1);
    }

    public static int getPlayTime(String serverIp) {
        return serverPlayTimes.getOrDefault(serverIp, 0);
    }
}