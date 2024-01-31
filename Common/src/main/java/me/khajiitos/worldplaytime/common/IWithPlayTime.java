package me.khajiitos.worldplaytime.common;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public interface IWithPlayTime {
    void setPlayTimeTicks(int playTimeTicks);
    int getPlayTimeTicks();

    default @Nullable Component getPlayTimeComponent() {
        int ticks = getPlayTimeTicks();

        if (ticks == -1) {
            return null;
        }

        double hours = (ticks / 20.0) / 3600.0;

        return Component.translatable("worldplaytime.format", Component.literal(hours >= 100.0 ? String.valueOf((int)hours) : String.format("%.1f", hours)));
    }
}
