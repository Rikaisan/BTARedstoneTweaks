package com.rikaisan.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicRepeater;
import net.minecraft.core.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLogicRepeater.class)
public abstract class BlockLogicRepeaterMixin extends BlockLogic {
	public BlockLogicRepeaterMixin(Block<?> block, Material material) {
		super(block, material);
	}
	// Ensure that repeaters can activate doors, tnt, and other redstone components through powering a neighboring block.
	@Override
	public boolean isSignalSource() {
		return true;
	}
}
