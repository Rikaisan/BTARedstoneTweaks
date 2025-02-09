package com.rikaisan;

public class AdditionalDoorTypeLogic {
	public static final int MASK_POWERED = 0b10000;
	public static boolean isPowered(int metadata) {
		return (metadata & MASK_POWERED) != 0;
	}
	public static int setPowered(int metadata, boolean isPowered) {
		return isPowered ? metadata | MASK_POWERED : metadata & ~MASK_POWERED;
	}
}
