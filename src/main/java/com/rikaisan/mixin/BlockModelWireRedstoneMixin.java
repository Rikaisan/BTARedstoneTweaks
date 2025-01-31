package com.rikaisan.mixin;

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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Environment(EnvType.CLIENT)
@Mixin(value = BlockModelWireRedstone.class, remap = false)
public abstract class BlockModelWireRedstoneMixin<T extends BlockLogic> extends BlockModelStandard<T> {

	public BlockModelWireRedstoneMixin(Block<T> block) {
		super(block);
	}

	@Redirect(
		method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;III)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z",
			ordinal = 1
		))
	public boolean shouldConnectToWest(WorldSource worldSource, int x, int y, int z, int data) {
		return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.TOP);
	}
	@Redirect(
		method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;III)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z",
			ordinal = 3
		))
	public boolean shouldConnectToEast(WorldSource worldSource, int x, int y, int z, int data) {
		return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.TOP);
	}
	@Redirect(
		method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;III)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z",
			ordinal = 5
		))
	public boolean shouldConnectToNorth(WorldSource worldSource, int x, int y, int z, int data) {
		return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.TOP);
	}
	@Redirect(
		method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;III)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z",
			ordinal = 7
		))
	public boolean shouldConnectToSouth(WorldSource worldSource, int x, int y, int z, int data) {
		return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.TOP);
	}

	@Redirect(
		method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;III)Z",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z",
				ordinal = 8
			),
			to = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z",
				ordinal = 11
			)
		),
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z"
		)
	)
	public boolean shouldConnectToUp(WorldSource worldSource, int x, int y, int z, int data) {
		return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.BOTTOM);
	}
}
