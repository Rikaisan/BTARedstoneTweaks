package com.rikaisan;

import net.minecraft.core.world.World;

public class AdditionalDoorTypeLogic {
	public static final int MASK_POWERED = 0b10000;

	public static boolean isPowered(int metadata) {
		return (metadata & MASK_POWERED) != 0;
	}

	public static int setPowered(int metadata, boolean isPowered) {
		return isPowered ? metadata | MASK_POWERED : metadata & ~MASK_POWERED;
	}

	public static int savePowered(World world, int x, int y, int z, int meta, boolean isPowered, boolean isPreviouslyPowered) {
		if (isPowered == isPreviouslyPowered) return meta;
		meta = AdditionalDoorTypeLogic.setPowered(meta, isPowered);
		world.setBlockMetadata(x, y, z, meta);
		return meta;
	}
}
