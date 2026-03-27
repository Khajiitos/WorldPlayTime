package me.khajiitos.worldplaytime.common.mixin;

import me.khajiitos.worldplaytime.common.config.WPTConfig;
import me.khajiitos.worldplaytime.common.util.IWithPlayTime;
import me.khajiitos.worldplaytime.common.util.PlayTimeRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
    LevelSummary summary;

    @Inject(at = @At("TAIL"), method = "extractContent", remap = false)
    public void render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, boolean pHovering, float pPartialTick, CallbackInfo ci) {
        if (!WPTConfig.showWorldPlayTime.get()) {
            return;
        }

        if (this.summary instanceof IWithPlayTime withPlayTime) {
            WorldSelectionList.WorldListEntry entry = (WorldSelectionList.WorldListEntry)(Object) this;
            int ticks = withPlayTime.worldPlayTime$getPlayTimeTicks();
            int indicatorWidth = PlayTimeRenderer.getWholeWidth(ticks);

            if (indicatorWidth != 0) {
                int renderX, renderY;

                switch (WPTConfig.worldPlayTimePosition.get()) {
                    case TOP_RIGHT -> {
                        renderX = entry.getContentX() + entry.getContentWidth() - indicatorWidth - 1;
                        renderY = entry.getContentY();
                    }
                    case LEFT -> {
                        renderX = entry.getContentX() - indicatorWidth - 5;
                        renderY = entry.getContentY() + 10;
                    }
                    case RIGHT -> {
                        renderX = entry.getContentX() + entry.getContentWidth() + 14;
                        renderY = entry.getContentY() + 10;
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