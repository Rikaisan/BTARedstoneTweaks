package com.rikaisan.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.rikaisan.RedstoneTweaks;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

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

	// Remove initial repeater update to prevent a random pulse on certain circumstances, but breaks /setBlock
	@WrapMethod(method = "onBlockPlacedByWorld(Lnet/minecraft/core/world/World;III)V")
	private void onBlockPlacedByWorld(World world, int x, int y, int z, Operation<Void> original) {
		if (!world.getGameRuleValue(RedstoneTweaks.REMOVE_INITIAL_REPEATER_UPDATE)) {
			original.call(world, x, y, z);
		}
	}
}
