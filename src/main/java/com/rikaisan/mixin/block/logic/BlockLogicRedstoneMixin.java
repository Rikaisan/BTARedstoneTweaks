package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.rikaisan.RedstoneTweaks;
import net.minecraft.core.block.BlockLogicRedstone;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockLogicRedstone.class, remap = false)
public abstract class BlockLogicRedstoneMixin {

	/// Make redstone blocks weak power instead of strong power, unless the relevant gamerule is set.
	@WrapMethod(method = "isEmittingDirectSignal(Lnet/minecraft/core/world/World;Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/util/helper/Side;)Z")
	public boolean isEmittingDirectSignal(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, Operation<Boolean> original) {
		if(world.getGameRuleValue(RedstoneTweaks.REDSTONE_BLOCK_HARD_POWER)) return original.call(world, tilePos, side);
		return false;
	}
}
