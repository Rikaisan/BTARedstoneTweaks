package com.rikaisan.mixin.block.logic;

import net.minecraft.core.block.BlockLogicLever;
import net.minecraft.core.util.helper.Axis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = BlockLogicLever.class, remap = false)
public class BlockLogicLeverMixin {

	@Redirect(method = "onBlockPlacedByMob", at = @At(value = "FIELD", target = "Lnet/minecraft/core/util/helper/Axis;Z:Lnet/minecraft/core/util/helper/Axis;"))
	private Axis useOtherAxis() {
		return Axis.X;
	}
}
