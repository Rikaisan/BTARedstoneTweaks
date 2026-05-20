package com.rikaisan;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;

public class AdditionalRedstoneWireLogic {
	public static int MASK_DIRECTION = 0xF0;
	public static int MASK_POWER = 0x0F;

	public static boolean shouldConnectToDiagonal(WorldSource worldSource, int x, int y, int z, int data, Side side) {
		if (!(side == Side.TOP || side == Side.BOTTOM)) return false; // Maybe some can generate a connection if placed below a block in the future?

		int blockId = worldSource.getBlockType(new TilePos(x, y, z)).id();
		return blockId == Blocks.WIRE_REDSTONE.id();
	}
}
