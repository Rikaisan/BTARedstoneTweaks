package com.rikaisan.mixin.block.logic;

import net.minecraft.core.block.BlockLogicMesh;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicMesh.class, remap = false)
public class BlockLogicMeshMixin {
	@Inject(method = "collidesWithEntity(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/world/World;Lnet/minecraft/core/world/pos/TilePosc;)Z", at = @At("TAIL"), cancellable = true)
	private void checkForPower(@NotNull Entity entity, @NotNull World world, @NotNull TilePosc tilePos, CallbackInfoReturnable<Boolean> cir) {
		if (world.hasNeighborSignal(tilePos)) cir.setReturnValue(true);
	}
}
