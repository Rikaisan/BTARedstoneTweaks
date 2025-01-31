package com.rikaisan.mixin;

import net.minecraft.core.block.BlockLogicRedstone;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlockLogicRedstone.class, remap = false)
public abstract class BlockLogicRedstoneMixin {

	/**
	 * @author Rikai
	 * @reason Make redstone blocks not hard power surrounding blocks
	 */
	@Overwrite
	public boolean getDirectSignal(World world, int x, int y, int z, Side side) {
		return false;
	}
}
