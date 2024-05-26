package me.khajiitos.worldplaytime.forge;

import me.khajiitos.worldplaytime.common.WorldPlayTime;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigCheck;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigScreenMaker;
import me.khajiitos.worldplaytime.common.handler.EventHandlerCommon;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod(WorldPlayTime.MOD_ID)
public class WorldPlayTimeForge {
    public WorldPlayTimeForge() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            WorldPlayTime.init();

            MinecraftForge.EVENT_BUS.addListener(WorldPlayTimeForge::onClientTick);
            MinecraftForge.EVENT_BUS.addListener(WorldPlayTimeForge::onShutDown);
            MinecraftForge.EVENT_BUS.addListener(WorldPlayTimeForge::onLoggedOut);


            if (ClothConfigCheck.isInstalled()) {
                ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((Function<Screen, Screen>) ClothConfigScreenMaker::create));
            }
        });
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