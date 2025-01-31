package com.rikaisan.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.block.BlockLogicRepeater;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
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
	// Cause block updates on the target block when removing a repeater,
	// which prevents redstone components on the other wide of the target block from staying powered.
	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		Side facing = BlockLogicBed.headBlockToFootBlockMap[BlockLogicBed.footToHeadMap[data & 3]];
		world.notifyBlocksOfNeighborChange(x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ(), id());
	}
}
