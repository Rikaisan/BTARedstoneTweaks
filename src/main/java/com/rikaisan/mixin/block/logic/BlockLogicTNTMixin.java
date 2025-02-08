package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.block.BlockLogicTNT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicTNT.class ,remap = false)
public class BlockLogicTNTMixin {

	@ModifyExpressionValue(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSignalSource()Z"))
	private boolean alwaysUpdate(boolean original) {
		return true;
	}
}
