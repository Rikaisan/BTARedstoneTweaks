package com.rikaisan.mixin;

import com.rikaisan.RedstoneTweaks;
import net.minecraft.core.block.BlockLogicRedstone;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicRedstone.class, remap = false)
public abstract class BlockLogicRedstoneMixin {

	@Inject(method = "getDirectSignal(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/util/helper/Side;)Z", at = @At("RETURN"), cancellable = true)
	public void getDirectSignal(World world, int x, int y, int z, Side side, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(world.getGameRuleValue(RedstoneTweaks.REDSTONE_BLOCK_HARD_POWER));
	}
}
