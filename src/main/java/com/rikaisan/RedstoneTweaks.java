package com.rikaisan;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class RedstoneTweaks implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "redstonetweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static GameRuleBoolean REDSTONE_BLOCK_HARD_POWER = GameRules.register(new GameRuleBoolean("redstoneBlockHardPower", false));
	public static GameRuleBoolean REMOVE_INITIAL_REPEATER_UPDATE = GameRules.register(new GameRuleBoolean("removeInitialRepeaterUpdate", true));
	public static GameRuleBoolean USE_ALTERNATE_CURRENT = GameRules.register(new GameRuleBoolean("useAlternateCurrent", true));

	@Override
    public void onInitialize() {
        LOGGER.info("Redstone Tweaks initialized.");
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}
}
