package me.khajiitos.worldplaytime.common;

import me.khajiitos.worldplaytime.common.config.ServerPlayTimeManager;
import me.khajiitos.worldplaytime.common.config.WPTConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldPlayTime {
    public static final String MOD_ID = "worldplaytime";
    public static final Logger LOGGER = LoggerFactory.getLogger("WorldPlayTime");

    public static void init() {
        WPTConfig.init();
        ServerPlayTimeManager.load();
    }
}
