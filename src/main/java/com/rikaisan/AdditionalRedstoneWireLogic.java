package com.rikaisan;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

public class AdditionalRedstoneWireLogic {
	public static boolean shouldConnectToDiagonal(WorldSource worldSource, int x, int y, int z, int data, Side side) {
		if (!(side == Side.TOP || side == Side.BOTTOM)) return false; // Maybe some can generate a connection if placed below a block in the future?

		return worldSource.getBlock(x, y, z) == Blocks.WIRE_REDSTONE;
	}
}
