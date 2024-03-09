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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerSelectionList.OnlineServerEntry.class, priority = 2000)
public class OnlineServerEntryMixin {

    @Shadow @Final private ServerData serverData;

    @Shadow @Final private Minecraft minecraft;

    @Unique
    private static int worldplaytime$serverNameStartX;

    @ModifyArg(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I",
                    ordinal = 0
            ),
            method = "render",
            index = 2
    )
    public int serverNameX(int x) {
        worldplaytime$serverNameStartX = x;
        return x;
    }

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
                int serverNameWidth = this.minecraft.font.width(serverData.name);
                renderX = worldplaytime$serverNameStartX + 3 + serverNameWidth;
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
