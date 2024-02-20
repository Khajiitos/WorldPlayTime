package me.khajiitos.worldplaytime.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.worldplaytime.common.config.ServerPlayTimeManager;
import me.khajiitos.worldplaytime.common.config.WPTConfig;
import me.khajiitos.worldplaytime.common.util.PlayTimeRenderer;
import me.khajiitos.worldplaytime.common.util.ServerEntryRenderPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public class OnlineServerEntryMixin {

    @Shadow @Final private ServerData serverData;

    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("TAIL"), method = "render")
    public void onRender(PoseStack poseStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (!WPTConfig.showServerPlayTime.get()) {
            return;
        }

        int playTime = ServerPlayTimeManager.getPlayTime(serverData.ip);
        int playTimeWidth = PlayTimeRenderer.getWholeWidth(playTime);

        if (playTimeWidth <= 0) {
            return;
        }

        int renderX, renderY;
        ServerEntryRenderPos renderPos = WPTConfig.serverPlayTimePosition.get();

        switch (renderPos) {
            case AFTER_NAME -> {
                // TODO: Server Country Flags integration - "Behind Name" option might cause issues
                int serverNameWidth = this.minecraft.font.width(serverData.name);
                renderX = x + 38 + serverNameWidth;
                renderY = y + 1;
            }
            case BEHIND_COUNT -> {
                int statusWidth = this.minecraft.font.width(serverData.status);
                renderX = x + entryWidth - 52 - statusWidth;
                renderY = y;
            }
            case LEFT -> {
                renderX = x - playTimeWidth - 5;
                renderY = y + 10;
            }
            case RIGHT -> {
                renderX = x + entryWidth + 6;
                renderY = y + 10;
            }
            default -> {
                return;
            }
        }

        PlayTimeRenderer.render(poseStack, renderX, renderY, playTime, WPTConfig.serverPlayTimeColor.get());
    }
}
