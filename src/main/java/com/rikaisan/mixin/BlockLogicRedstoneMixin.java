package com.rikaisan.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.rikaisan.RedstoneTweaks;
import net.minecraft.core.block.BlockLogicRedstone;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockLogicRedstone.class, remap = false)
public abstract class BlockLogicRedstoneMixin {

	@WrapMethod(method = "getDirectSignal(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/util/helper/Side;)Z")
	public boolean getDirectSignal(World world, int x, int y, int z, Side side, Operation<Boolean> original) {
		if(world.getGameRuleValue(RedstoneTweaks.REDSTONE_BLOCK_HARD_POWER)) return original.call(world, x, y, z, side);
		return false;
	}
}
