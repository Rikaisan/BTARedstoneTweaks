package com.rikaisan.mixin.block.logic;

import net.minecraft.core.block.BlockLogicMesh;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicMesh.class, remap = false)
public class BlockLogicMeshMixin {
	@Inject(method = "collidesWithEntity(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/world/World;III)Z", at = @At("TAIL"), cancellable = true)
	private void checkForPower(Entity entity, World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if (world.hasNeighborSignal(new TilePos(x, y, z))) cir.setReturnValue(true);
	}
}
