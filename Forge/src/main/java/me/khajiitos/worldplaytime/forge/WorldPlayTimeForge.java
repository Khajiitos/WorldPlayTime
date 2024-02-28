package me.khajiitos.worldplaytime.forge;

import me.khajiitos.worldplaytime.common.WorldPlayTime;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigCheck;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigScreenMaker;
import me.khajiitos.worldplaytime.common.handler.EventHandlerCommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod(WorldPlayTime.MOD_ID)
public class WorldPlayTimeForge {
    public WorldPlayTimeForge() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            WorldPlayTime.init();

            MinecraftForge.EVENT_BUS.addListener(WorldPlayTimeForge::onClientTick);
            //MinecraftForge.EVENT_BUS.addListener(WorldPlayTimeForge::onShutDown);
            MinecraftForge.EVENT_BUS.addListener(WorldPlayTimeForge::onLoggedOut);

            if (ClothConfigCheck.isInstalled()) {
                ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory(ClothConfigScreenMaker::create));
            }

            ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        });
    }

    private static void onClientTick(TickEvent.ClientTickEvent e) {
        EventHandlerCommon.onClientTick();
    }

    // TODO: replace it with another event, if one exists?
    /*
    private static void onShutDown( e) {
        EventHandlerCommon.onLeavingGame();
    }*/

    private static void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent e) {
        EventHandlerCommon.onLeaveServer();
    }
}