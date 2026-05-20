package com.rikaisan.mixin.item;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFarmland;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemSeeds;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ItemSeeds.class, remap = false)
public abstract class ItemSeedsMixin {

	/// Make farmland pass the check to use the seeds
	@Definition(id = "b", local = @Local(type = Block.class))
	@Expression("b == null")
	@ModifyExpressionValue(
		method = "onUseByActivator(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/block/entity/TileEntityActivator;Lnet/minecraft/core/world/World;Ljava/util/Random;IIIDDDLnet/minecraft/core/util/helper/Direction;)V",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	public boolean isFarmland(boolean original, @Local(name = "b") Block<?> block) {
		return original || block.getLogic() instanceof BlockLogicFarmland;
	}


	/// Make the action take place above the farmland
	@WrapOperation(
		method = "onUseByActivator(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/block/entity/TileEntityActivator;Lnet/minecraft/core/world/World;Ljava/util/Random;IIIDDDLnet/minecraft/core/util/helper/Direction;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/item/ItemSeeds;onUseItemOnBlock(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/entity/player/Player;Lnet/minecraft/core/world/World;IIILnet/minecraft/core/util/helper/Side;DD)Z"
		)
	)
	public boolean onUseItemOnBlock(
		ItemSeeds instance,
		ItemStack itemstack,
		Player entityplayer,
		World world,
		int blockX,
		int blockY,
		int blockZ,
		Side side,
		double xPlaced,
		double yPlaced,
		Operation<Boolean> original
	) {
		Block<?> block = world.getBlockType(new TilePos(blockX, blockY, blockZ));
		if (block != null && block.getLogic() instanceof BlockLogicFarmland) {
			blockY += 1;
		}
		return original.call(instance, itemstack, entityplayer, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
	}
}
