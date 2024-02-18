package me.khajiitos.worldplaytime.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigCheck;
import me.khajiitos.worldplaytime.common.config.cloth.ClothConfigScreenMaker;

public class ModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (ClothConfigCheck.isInstalled()) {
            return ClothConfigScreenMaker::create;
        }
        return ModMenuApi.super.getModConfigScreenFactory();
    }
}