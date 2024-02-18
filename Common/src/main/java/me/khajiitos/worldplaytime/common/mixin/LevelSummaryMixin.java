package me.khajiitos.worldplaytime.common.mixin;

import me.khajiitos.worldplaytime.common.util.IWithPlayTime;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelSummary.class)
public class LevelSummaryMixin implements IWithPlayTime {
    @Unique
    private int worldplaytime$playTimeTicks = -1;

    @Override
    public void setPlayTimeTicks(int playTimeTicks) {
        this.worldplaytime$playTimeTicks = playTimeTicks;
    }

    @Override
    public int getPlayTimeTicks() {
        return this.worldplaytime$playTimeTicks;
    }
}
