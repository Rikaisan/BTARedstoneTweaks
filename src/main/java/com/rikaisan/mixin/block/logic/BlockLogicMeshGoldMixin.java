package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.BlockLogicMeshGold;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicMeshGold.class, remap = false)
public class BlockLogicMeshGoldMixin {
	@ModifyExpressionValue(method = "collidesWithEntity(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/world/World;Lnet/minecraft/core/world/pos/TilePosc;)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/core/block/entity/TileEntityMeshGold;filterItem:Lnet/minecraft/core/item/ItemStack;"))
	private ItemStack checkForPower(ItemStack original, @Local(name = "world") World world, @Local(name = "tilePos") TilePosc tilePos) {
		if (world.hasNeighborSignal(tilePos)) return null;
		return original;
	}
}
