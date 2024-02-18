package me.khajiitos.worldplaytime.common.handler;

import me.khajiitos.worldplaytime.common.config.ServerPlayTimeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;

public class EventHandlerCommon {

    private static int playTicks;

    public static void onClientTick() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if (connection == null) {
            return;
        }

        ServerData serverData = connection.getServerData();

        if (serverData == null) {
            return;
        }

        String connectedServer = serverData.ip;

        ServerPlayTimeManager.onPlayTick(connectedServer);

        if (++playTicks >= 6000) {
            ServerPlayTimeManager.saveAsync();
            playTicks = 0;
        }
    }

    public static void onLeavingGame() {
        ServerPlayTimeManager.save();
    }

    public static void onLeaveServer() {
        ServerPlayTimeManager.save();
    }
}
