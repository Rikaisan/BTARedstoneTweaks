package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.rikaisan.AdditionalDoorTypeLogic;
import net.minecraft.core.block.BlockLogicDoor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// DISABLED DUE TO INCOMPATIBILITY WITH COLORS.
@Mixin(value = BlockLogicDoor.class, remap = false)
public class BlockLogicDoorMixin {
	@Inject(method = "onBlockPlacedOnSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockMetadataWithNotify(IIII)V"))
	private void loadPreviouslyPoweredBlockPlaced(World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfo ci, @Local(name = "meta") LocalIntRef meta) {
		boolean isPreviouslyPowered = AdditionalDoorTypeLogic.isPowered(meta.get());
		meta.set(AdditionalDoorTypeLogic.savePowered(world, x, y, z, meta.get(), true, isPreviouslyPowered));
	}

	@Inject(method = "onPoweredBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockMetadata(III)I", ordinal = 1, shift = At.Shift.AFTER))
	private void loadPreviouslyPoweredBlockChanged(World world, int x, int y, int z, boolean isPowered, CallbackInfo ci, @Local(name = "meta") LocalIntRef meta, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		isPreviouslyPowered.set(AdditionalDoorTypeLogic.isPowered(meta.get()));
		meta.set(AdditionalDoorTypeLogic.savePowered(world, x, y, z, meta.get(), isPowered, isPreviouslyPowered.get()));
	}

	@Definition(id = "isOpen", local = @Local(type = boolean.class, ordinal = 1))
	@Definition(id = "isPowered", local = @Local(type = boolean.class, ordinal = 0, argsOnly = true))
	@Expression("isOpen != isPowered")
	@ModifyExpressionValue(method = "onPoweredBlockChange", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkPoweredChanged(boolean original, @Local(name = "isPowered") boolean isPowered, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		return original && isPowered != isPreviouslyPowered.get();
	}

	@ModifyExpressionValue(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSignalSource()Z"))
	private boolean alwaysUpdate(boolean original) {
		return true;
	}
}
