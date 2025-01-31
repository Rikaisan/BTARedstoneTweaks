package com.rikaisan.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {

	/**
	 * @author Rikai
	 * @reason Make redstone pumpkin behave like a solid block
	 */
	@Overwrite
	public boolean getSignal(int x, int y, int z, Side side) {
		Block<?> block = this.getBlock(x, y, z);
		if (this.isBlockNormalCube(x, y, z) && block != Blocks.BLOCK_REDSTONE && block != Blocks.PUMPKIN_REDSTONE) {
			return this.hasDirectSignal(x, y, z);
		} else if (block == Blocks.PUMPKIN_REDSTONE) { // Patch to make the redstone pumpkin behave like a solid block
			return this.hasDirectSignal(x, y, z) || block.getSignal((WorldSource) this, x, y, z, side);
		}
		else {
			return block != null && block.getSignal((WorldSource) this, x, y, z, side);
		}
	}

	@Shadow
	public abstract boolean isBlockNormalCube(int x, int y, int z);

	@Shadow
	public abstract boolean hasDirectSignal(int x, int y, int z);

	@Shadow
	public abstract Block<?> getBlock(int x, int y, int z);
}

