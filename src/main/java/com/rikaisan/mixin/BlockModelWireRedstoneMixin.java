package com.rikaisan.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.rikaisan.AdditionalRedstoneWireLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.block.model.BlockModelWireRedstone;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = BlockModelWireRedstone.class, remap = false)
public abstract class BlockModelWireRedstoneMixin<T extends BlockLogic> extends BlockModelStandard<T> {

	public BlockModelWireRedstoneMixin(Block<T> block) {
		super(block);
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z"))
	private boolean a(WorldSource worldSource, int x, int y, int z, int data, Operation<Boolean> original, @Local(name = "y") int originalY) {
		if(y == originalY - 1) return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.TOP);
		if(y == originalY + 1) return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.BOTTOM);
		return original.call(worldSource, x, y, z, data);
	}
}
