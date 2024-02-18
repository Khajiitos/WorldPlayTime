package me.khajiitos.worldplaytime.common.util;

import me.khajiitos.worldplaytime.common.WorldPlayTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PlayTimeRenderer {

    private static final ResourceLocation TIME_ICON = new ResourceLocation(WorldPlayTime.MOD_ID, "textures/gui/time_icon.png");

    public static @Nullable Component getPlayTimeComponent(int ticks) {
        if (ticks <= 0) {
            return null;
        }

        double hours = (ticks / 20.0) / 3600.0;

        return Component.translatable("worldplaytime.format", Component.literal(hours >= 100.0 ? String.valueOf((int)hours) : String.format("%.1f", hours)));
    }

    public static int getWholeWidth(int ticks) {
        Component component = getPlayTimeComponent(ticks);

        if (component == null) {
            return 0;
        }

        Minecraft minecraft = Minecraft.getInstance();

        return minecraft.font.width(component) + 11;
    }

    public static void render(GuiGraphics guiGraphics, int x, int y, int playTimeTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Component component = getPlayTimeComponent(playTimeTicks);

        if (component == null) {
            return;
        }

        guiGraphics.blit(TIME_ICON, x, y, 0.f, 0.f, 9, 9, 9, 9);
        guiGraphics.drawString(minecraft.font, component, x + 11, y + 1, 0xFF808080, false);
    }
}
