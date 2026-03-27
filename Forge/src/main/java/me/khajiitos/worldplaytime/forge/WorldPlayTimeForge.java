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
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

@Mod(WorldPlayTime.MOD_ID)
public class WorldPlayTimeForge {
    public WorldPlayTimeForge(FMLJavaModLoadingContext context) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            WorldPlayTime.init();
            MinecraftForge.EVENT_BUS.register(WorldPlayTimeForge.class);

            if (ClothConfigCheck.isInstalled()) {
                context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((Function<Screen, Screen>) ClothConfigScreenMaker::create));
            }
        }
    }

    @SubscribeEvent
    private static void onClientTick(TickEvent.ClientTickEvent e) {
        EventHandlerCommon.onClientTick();
    }

    @SubscribeEvent
    private static void onShutDown(GameShuttingDownEvent e) {
        EventHandlerCommon.onLeavingGame();
    }

    @SubscribeEvent
    private static void onLoggedOut(ClientPlayerNetworkEvent.LoggingOut e) {
        EventHandlerCommon.onLeaveServer();
    }
}