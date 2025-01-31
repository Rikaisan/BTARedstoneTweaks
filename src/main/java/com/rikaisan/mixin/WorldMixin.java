package com.rikaisan.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {

	@Redirect(method = "getSignal(IIILnet/minecraft/core/util/helper/Side;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;getSignal(Lnet/minecraft/core/world/WorldSource;IIILnet/minecraft/core/util/helper/Side;)Z"))
	public boolean getSignal(Block<?> instance, WorldSource worldSource, int x, int y, int z, Side side) {
		if (instance == Blocks.PUMPKIN_REDSTONE) {
			return pumpkinHasDirectSignal(x, y, z) || instance.getSignal(worldSource, x, y, z, side);
		} else {
			return instance.getSignal(worldSource, x, y, z, side);
		}
	}

	// Special version of hasDirectSignal that excludes the front side of the redstone lantern.
	@Unique
	private boolean pumpkinHasDirectSignal(int x, int y, int z) {
		for (Side side : Side.sides) {
			if(Side.getSideById(getBlockMetadata(x, y, z)) == side) continue;
			if (getDirectSignal(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ(), side)) {
				return true;
			}
		}

		return false;
	}

	@Shadow
	public abstract boolean getDirectSignal(int x, int y, int z, Side side);

	@Shadow
	public abstract int getBlockMetadata(int x, int y, int z);
}

