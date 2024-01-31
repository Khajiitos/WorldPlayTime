package me.khajiitos.worldplaytime.common.mixin;

import me.khajiitos.worldplaytime.common.IWithPlayTime;
import me.khajiitos.worldplaytime.common.WorldPlayTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldListEntryMixin {
    @Unique
    private static final ResourceLocation TIME_ICON = new ResourceLocation(WorldPlayTime.MOD_ID, "textures/gui/time_icon.png");

    @Shadow @Final private LevelSummary summary;

    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick, CallbackInfo ci) {
        if (this.summary instanceof IWithPlayTime withPlayTime) {
            Component component = withPlayTime.getPlayTimeComponent();

            if (component != null) {
                int componentWidth = this.minecraft.font.width(component);
                guiGraphics.blit(TIME_ICON, x + width - componentWidth - 15, y, 0.f, 0.f, 9, 9, 9, 9);
                guiGraphics.drawString(this.minecraft.font, component, x + width - componentWidth - 4, y + 1, 0xFF808080, false);
            }
        }
    }
}
