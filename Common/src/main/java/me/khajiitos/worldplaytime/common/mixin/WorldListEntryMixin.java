package me.khajiitos.worldplaytime.common.mixin;

import me.khajiitos.worldplaytime.common.config.WPTConfig;
import me.khajiitos.worldplaytime.common.util.IWithPlayTime;
import me.khajiitos.worldplaytime.common.util.PlayTimeRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldListEntryMixin {
    @Shadow @Final
    private LevelSummary summary;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick, CallbackInfo ci) {
        if (!WPTConfig.showWorldPlayTime.get()) {
            return;
        }

        if (this.summary instanceof IWithPlayTime withPlayTime) {
            int ticks = withPlayTime.getPlayTimeTicks();
            int indicatorWidth = PlayTimeRenderer.getWholeWidth(ticks);

            if (indicatorWidth != 0) {
                int renderX, renderY;

                switch (WPTConfig.worldPlayTimePosition.get()) {
                    case TOP_RIGHT -> {
                        renderX = x + width - indicatorWidth - 4;
                        renderY = y;
                    }
                    case LEFT -> {
                        renderX = x - indicatorWidth - 5;
                        renderY = y + 10;
                    }
                    case RIGHT -> {
                        renderX = x + width + 14;
                        renderY = y + 10;
                    }
                    default -> {
                        return;
                    }
                }

                PlayTimeRenderer.render(guiGraphics, renderX, renderY, ticks, WPTConfig.worldPlayTimeColor.get());
            }
        }
    }
}