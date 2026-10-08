package com.rikaisan;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedstoneTweaks implements ModInitializer {
    public static final String MOD_ID = "redstonetweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static GameRuleBoolean REDSTONE_BLOCK_HARD_POWER = GameRules.register(new GameRuleBoolean("redstoneBlockHardPower", "redstone_tweaks.redstone_block_hard_power", false));

	@Override
    public void onInitialize() {
        LOGGER.info("Redstone Tweaks initialized.");
    }
}
