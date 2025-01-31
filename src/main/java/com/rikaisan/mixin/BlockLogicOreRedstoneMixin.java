package com.rikaisan.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicOreRedstone;
import net.minecraft.core.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLogicOreRedstone.class)
public abstract class BlockLogicOreRedstoneMixin extends BlockLogic {

	public BlockLogicOreRedstoneMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public boolean isSignalSource() {
		return true;
	}
}
