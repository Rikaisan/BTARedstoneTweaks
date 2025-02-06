package com.rikaisan.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicRepeater.class, remap = false)
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
		Side front = BlockLogicBed.headBlockToFootBlockMap[BlockLogicBed.footToHeadMap[data & 3]];
		world.notifyBlocksOfNeighborChange(x + front.getOffsetX(), y + front.getOffsetY(), z + front.getOffsetZ(), id());
		Side back = front.getOpposite();
		world.notifyBlocksOfNeighborChange(x + back.getOffsetX(), y + back.getOffsetY(), z + back.getOffsetZ(), id());
	}

	// Fix the 1 tick pulse when placing a repeater next to a powered block
	@ModifyExpressionValue(method = "updateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/block/BlockLogicRepeater;isRepeaterPowered:Z", ordinal = 1))
	private boolean checkGettingPowered(boolean original, @Local(name = "flag") boolean flag) {
		return original || !flag;
	}
}
