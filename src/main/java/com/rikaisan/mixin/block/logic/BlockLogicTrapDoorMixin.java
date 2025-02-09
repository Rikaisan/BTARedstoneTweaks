package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.rikaisan.AdditionalDoorTypeLogic;
import net.minecraft.core.block.BlockLogicTrapDoor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockLogicTrapDoor.class, remap = false)
public class BlockLogicTrapDoorMixin {

	@Definition(id = "isOpened", local = @Local(type = boolean.class, ordinal = 0))
	@Definition(id = "isPowered", local = @Local(type = boolean.class, ordinal = 1))
	@Expression("isOpened != isPowered")
	@Inject(method = "onNeighborBlockChange", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void loadPreviouslyPoweredNeighborUpdate(World world, int x, int y, int z, int blockId, CallbackInfo ci, @Local(name = "meta") LocalIntRef meta, @Local(name = "isPowered") boolean isPowered, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		isPreviouslyPowered.set(AdditionalDoorTypeLogic.isPowered(meta.get()));
		meta.set(AdditionalDoorTypeLogic.savePowered(world, x, y, z, meta.get(), isPowered, isPreviouslyPowered.get()));
	}

	@Definition(id = "isOpened", local = @Local(type = boolean.class, ordinal = 0))
	@Definition(id = "isPowered", local = @Local(type = boolean.class, ordinal = 1))
	@Expression("isOpened != isPowered")
	@Inject(method = "onBlockPlacedOnSide", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void loadPreviouslyPoweredBlockPlaced(World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfo ci, @Local(name = "meta") LocalIntRef meta, @Local(name = "isPowered") boolean isPowered, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		isPreviouslyPowered.set(AdditionalDoorTypeLogic.isPowered(meta.get()));
		meta.set(AdditionalDoorTypeLogic.savePowered(world, x, y, z, meta.get(), isPowered, isPreviouslyPowered.get()));
	}

	@Definition(id = "isOpened", local = @Local(type = boolean.class, ordinal = 0))
	@Definition(id = "isPowered", local = @Local(type = boolean.class, ordinal = 1))
	@Expression("isOpened != isPowered")
	@ModifyExpressionValue(method = "onNeighborBlockChange", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkPoweredChanged(boolean original, @Local(name = "isPowered") boolean isPowered, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		return original && isPowered != isPreviouslyPowered.get();
	}

	@ModifyExpressionValue(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSignalSource()Z"))
	private boolean alwaysUpdate(boolean original) {
		return true;
	}
}
