package me.khajiitos.worldplaytime.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.worldplaytime.common.util.IWithPlayTime;
import me.khajiitos.worldplaytime.common.util.PlayTimeRenderer;
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
    public void render(PoseStack poseStack, int index, int y, int x, int width, int height, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick, CallbackInfo ci) {
        if (this.summary instanceof IWithPlayTime withPlayTime) {
            int ticks = withPlayTime.getPlayTimeTicks();
            int indicatorWidth = PlayTimeRenderer.getWholeWidth(ticks);

            if (indicatorWidth != 0) {
                PlayTimeRenderer.render(poseStack, x + width - indicatorWidth - 3, y + 1, ticks);
            }
        }
    }
}
