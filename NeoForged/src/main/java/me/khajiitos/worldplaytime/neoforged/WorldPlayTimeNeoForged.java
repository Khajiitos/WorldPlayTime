package me.khajiitos.worldplaytime.neoforged;

import me.khajiitos.worldplaytime.common.WorldPlayTime;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigCheck;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigScreenMaker;
import me.khajiitos.worldplaytime.common.handler.EventHandlerCommon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.TickEvent;

@Mod(WorldPlayTime.MOD_ID)
public class WorldPlayTimeNeoForged {
    public WorldPlayTimeNeoForged() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            WorldPlayTime.init();

            NeoForge.EVENT_BUS.addListener(WorldPlayTimeNeoForged::onClientTick);
            NeoForge.EVENT_BUS.addListener(WorldPlayTimeNeoForged::onShutDown);
            NeoForge.EVENT_BUS.addListener(WorldPlayTimeNeoForged::onLoggedOut);

            if (ClothConfigCheck.isInstalled()) {
                ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(ClothConfigScreenMaker::create));
            }

        }
    }

    private static void onClientTick(TickEvent.ClientTickEvent e) {
        EventHandlerCommon.onClientTick();
    }

    private static void onShutDown(GameShuttingDownEvent e) {
        EventHandlerCommon.onLeavingGame();
    }

    private static void onLoggedOut(ClientPlayerNetworkEvent.LoggingOut e) {
        EventHandlerCommon.onLeaveServer();
    }
}