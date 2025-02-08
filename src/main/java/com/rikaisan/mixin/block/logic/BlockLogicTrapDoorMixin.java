package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.rikaisan.AdditionalTrapDoorLogic;
import net.minecraft.core.block.BlockLogicTrapDoor;
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
	private void writeChangedPowered(World world, int x, int y, int z, int blockId, CallbackInfo ci, @Local(name = "meta") LocalIntRef meta, @Local(name = "isPowered") boolean isPowered, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		isPreviouslyPowered.set(AdditionalTrapDoorLogic.isPowered(meta.get()));
		if(isPowered != isPreviouslyPowered.get()) {
			meta.set(AdditionalTrapDoorLogic.setPowered(meta.get(), isPowered));
			world.setBlockMetadata(x, y, z, meta.get());
		}
	}

	@Definition(id = "isOpened", local = @Local(type = boolean.class, ordinal = 0))
	@Definition(id = "isPowered", local = @Local(type = boolean.class, ordinal = 1))
	@Expression("isOpened != isPowered")
	@ModifyExpressionValue(method = "onNeighborBlockChange", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean preventOpen(boolean original, @Local(name = "isPowered") boolean isPowered, @Share("isPreviouslyPowered") LocalBooleanRef isPreviouslyPowered) {
		return original && isPowered != isPreviouslyPowered.get();
	}
}
