package com.rikaisan.mixin.block.logic;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.rikaisan.AdditionalRedstoneWireLogic;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicWireRedstone.class, remap = false)
public abstract class BlockLogicWireRedstoneMixin extends BlockLogic {

	public BlockLogicWireRedstoneMixin(Block<?> block, Material material) {
		super(block, material);
	}

	// --------------------------------------------------------------------------------

	/// Fix for vertically diagonal signal sources, to make behaviour match modern vanilla redstone connections
	@WrapOperation(method = "getSignal", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogicWireRedstone;shouldConnectTo(Lnet/minecraft/core/world/WorldSource;IIII)Z"))
	private boolean fixDiagonal(WorldSource worldSource, int x, int y, int z, int data, Operation<Boolean> original, @Local(name = "y") int originalY) {
		if(y == originalY - 1) return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.TOP);
		if(y == originalY + 1) return AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y, z, data, Side.BOTTOM);
		return original.call(worldSource, x, y, z, data);
	}

	/// Refactor + Patch to make redirection work as intended
	@Definition(id = "negZShouldConnectTo", local = @Local(type = boolean.class, name = "negZShouldConnectTo"))
	@Expression("negZShouldConnectTo")
	@Inject(method = "getSignal", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), cancellable = true)
	private void fixRedirections(WorldSource worldSource, int x, int y, int z, Side side, CallbackInfoReturnable<Boolean> cir, @Local(name = "posXShouldConnectTo") boolean posXShouldConnectTo, @Local(name = "negXShouldConnectTo") boolean negXShouldConnectTo, @Local(name = "posZShouldConnectTo") boolean posZShouldConnectTo, @Local(name = "negZShouldConnectTo") boolean negZShouldConnectTo) {

		// Make wire emit block updates when a redirection happens
		if (worldSource instanceof World) {
			World world = (World) worldSource;

			int meta = world.getBlockData(new TilePos(x, y, z));
			int direction = meta & AdditionalRedstoneWireLogic.MASK_DIRECTION;

			int newDirectionNorth = (negZShouldConnectTo ? 1 : 0) << 4;
			int newDirectionSouth = (posZShouldConnectTo ? 1 : 0) << 5;
			int newDirectionWest  = (negXShouldConnectTo ? 1 : 0) << 6;
			int newDirectionEast  = (posXShouldConnectTo ? 1 : 0) << 7;
			int newDirection = newDirectionNorth | newDirectionSouth | newDirectionWest | newDirectionEast;

			if (direction != newDirection) {
				meta &= ~AdditionalRedstoneWireLogic.MASK_DIRECTION;
				meta |= newDirection;
				world.setBlockData(new TilePos(x, y, z), meta);
			}
		}


		boolean isXConnected = posXShouldConnectTo || negXShouldConnectTo;
		boolean isZConnected = posZShouldConnectTo || negZShouldConnectTo;
		cir.setReturnValue(!isZConnected && !isXConnected && side.axis() != Axis.Y // Default single dust
			// When the signal request comes from a block that dust connects to (usually power sources)
			|| side == Side.SOUTH && negZShouldConnectTo
			|| side == Side.NORTH && posZShouldConnectTo
			|| side == Side.EAST && negXShouldConnectTo
			|| side == Side.WEST && posXShouldConnectTo
			// When the signal request comes from the block opposite of a connection (most redstone components)
			|| side == Side.NORTH && negZShouldConnectTo && !isXConnected
			|| side == Side.SOUTH && posZShouldConnectTo && !isXConnected
			|| side == Side.WEST && negXShouldConnectTo && !isZConnected
			|| side == Side.EAST && posXShouldConnectTo && !isZConnected);
	}

	@Redirect(method = "updatePowerStrength(Lnet/minecraft/core/world/World;IIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockMetadataWithNotify(IIII)V"))
	void setWireSignal(World instance, int x, int y, int z, int power) {
		int direction = instance.getBlockData(new TilePos(x, y, z)) & AdditionalRedstoneWireLogic.MASK_DIRECTION;
		instance.setBlockDataNotify(new TilePos(x, y, z), power | direction);
	}

	@Redirect(method = "getSignal(Lnet/minecraft/core/world/WorldSource;IIILnet/minecraft/core/util/helper/Side;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMetadata(III)I"))
	int getRawSignal(WorldSource instance, int x, int y, int z) {
		return instance.getBlockData(new TilePos(x, y, z)) & AdditionalRedstoneWireLogic.MASK_POWER;
	}

	@Redirect(method = "updatePowerStrength(Lnet/minecraft/core/world/World;IIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockMetadata(III)I"))
	int getRawSignal2(World instance, int x, int y, int z) {
		return instance.getBlockData(new TilePos(x, y, z)) & AdditionalRedstoneWireLogic.MASK_POWER;
	}

	@Redirect(method = "checkTarget(Lnet/minecraft/core/world/World;IIII)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockMetadata(III)I"))
	int getRawSignal3(World instance, int x, int y, int z) {
		return instance.getBlockData(new TilePos(x, y, z)) & AdditionalRedstoneWireLogic.MASK_POWER;
	}

	@Redirect(method = "animationTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockMetadata(III)I"))
	int getRawSignal4(World instance, int x, int y, int z) {
		return instance.getBlockData(new TilePos(x, y, z)) & AdditionalRedstoneWireLogic.MASK_POWER;
	}

	@ModifyExpressionValue(method = "shouldConnectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSignalSource()Z"))
	private static boolean dontConnectRedstonePumpkin(boolean original, @Local(name = "blockId") int blockId) {
		return original && blockId != Blocks.PUMPKIN_REDSTONE.id();
	}

	/// Connect to both the front and back of the repeater, instead of only the back.
	@Inject(method = "shouldConnectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMetadata(III)I", ordinal = 1), cancellable = true)
	private static void connectToFrontBackRepeater(WorldSource worldSource, int x, int y, int z, int data, CallbackInfoReturnable<Boolean> cir) {
		// Get the requested side.
		Side source = BlockLogicBed.footToHeadMap[data];
		// Get the side the repeater is facing.
		Side target = BlockLogicBed.footToHeadMap[worldSource.getBlockData(new TilePos(x, y, z)) & 3];
		cir.setReturnValue(target == source || target == source.opposite());
	}

	/// Required for setting the repeater to be a signal source.
	/// Otherwise, connectToFrontBackRepeater will not be called, and redstone will redirect to all sides of the repeater.
	@ModifyExpressionValue(method = "shouldConnectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSignalSource()Z"))
	private static boolean dontConnectToAllRepeaterSides(boolean original, @Local(name = "blockId") int blockId) {
		return original && blockId != Blocks.REPEATER_IDLE.id() && blockId != Blocks.REPEATER_ACTIVE.id();
	}
}
