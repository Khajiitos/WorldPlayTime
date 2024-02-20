package me.khajiitos.worldplaytime.common.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.worldplaytime.common.WorldPlayTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
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

    public static void render(PoseStack poseStack, int x, int y, int playTimeTicks, Color color) {
        Minecraft minecraft = Minecraft.getInstance();
        Component component = getPlayTimeComponent(playTimeTicks);

        if (component == null) {
            return;
        }

        RenderSystem.setShaderColor(color.r / 255.f, color.g / 255.f, color.b / 255.f, color.a / 255.f);
        RenderSystem.setShaderTexture(0, TIME_ICON);
        GuiComponent.blit(poseStack, x, y, 0.f, 0.f, 9, 9, 9, 9);
        RenderSystem.setShaderColor(1.f, 1.f, 1.f, 1.f);
        minecraft.font.draw(poseStack, component, x + 11, y + 1, color.toARGB());
    }
}
