package com.rikaisan.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicOreRedstone;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLogicOreRedstone.class)
public abstract class BlockLogicOreRedstoneMixin extends BlockLogic {

	public BlockLogicOreRedstoneMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public void onBlockPlacedByWorld(World world, int x, int y, int z) {
		for (Side s : Side.sides) {
			world.notifyBlocksOfNeighborChange(x + s.getOffsetX(), y + s.getOffsetY(), z + s.getOffsetZ(), this.id());
		}
	}

	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		for (Side s : Side.sides) {
			world.notifyBlocksOfNeighborChange(x + s.getOffsetX(), y + s.getOffsetY(), z + s.getOffsetZ(), this.id());
		}
	}

	@Override
	public boolean isSignalSource() {
		return true;
	}
}
