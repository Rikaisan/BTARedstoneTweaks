package com.rikaisan.mixin;

import com.rikaisan.AdditionalRedstoneWireLogic;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.block.BlockLogicWireRedstone;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.core.block.BlockLogicWireRedstone.shouldConnectTo;

@Mixin(value = BlockLogicWireRedstone.class, remap = false)
public abstract class BlockLogicWireRedstoneMixin {
	@Shadow
	private boolean shouldSignal;

	/**
	 * @author Rikai
	 * @reason Refactor + Fix redirection
	 */
	@Overwrite
	public boolean getSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		if (!this.shouldSignal || worldSource.getBlockMetadata(x, y, z) == 0) {
			return false;
		} else if (side == Side.TOP) {
			return true;
		} else {
			boolean negXShouldConnectTo = shouldConnectTo(worldSource, x - 1, y, z, 1)
				|| !worldSource.isBlockNormalCube(x - 1, y, z) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x - 1, y - 1, z, -1, Side.TOP);
			boolean posXShouldConnectTo = shouldConnectTo(worldSource, x + 1, y, z, 3)
				|| !worldSource.isBlockNormalCube(x + 1, y, z) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x + 1, y - 1, z, -1, Side.TOP);
			boolean negZShouldConnectTo = shouldConnectTo(worldSource, x, y, z - 1, 2)
				|| !worldSource.isBlockNormalCube(x, y, z - 1) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y - 1, z - 1, -1, Side.TOP);
			boolean posZShouldConnectTo = shouldConnectTo(worldSource, x, y, z + 1, 0)
				|| !worldSource.isBlockNormalCube(x, y, z + 1) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y - 1, z + 1, -1, Side.TOP);

			if (!worldSource.isBlockNormalCube(x, y + 1, z)) {
				if (worldSource.isBlockNormalCube(x - 1, y, z) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x - 1, y + 1, z, -1, Side.BOTTOM)) {
					negXShouldConnectTo = true;
				}

				if (worldSource.isBlockNormalCube(x + 1, y, z) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x + 1, y + 1, z, -1, Side.BOTTOM)) {
					posXShouldConnectTo = true;
				}

				if (worldSource.isBlockNormalCube(x, y, z - 1) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y + 1, z - 1, -1, Side.BOTTOM)) {
					negZShouldConnectTo = true;
				}

				if (worldSource.isBlockNormalCube(x, y, z + 1) && AdditionalRedstoneWireLogic.shouldConnectToDiagonal(worldSource, x, y + 1, z + 1, -1, Side.BOTTOM)) {
					posZShouldConnectTo = true;
				}
			}

		boolean isXConnected = posXShouldConnectTo || negXShouldConnectTo;
		boolean isZConnected = posZShouldConnectTo || negZShouldConnectTo;

		// Refactor + Patch to make redirection work as intended
		return !isZConnected && !isXConnected && side.getAxis() != Axis.Y // Default single dust
			// When the signal request comes from a block that dust connects to (usually power sources)
			|| side == Side.SOUTH && negZShouldConnectTo
			|| side == Side.NORTH && posZShouldConnectTo
			|| side == Side.EAST && negXShouldConnectTo
			|| side == Side.WEST && posXShouldConnectTo
			// When the signal request comes from the block opposite of a connection (most redstone components)
			|| side == Side.NORTH && negZShouldConnectTo && !isXConnected
			|| side == Side.SOUTH && posZShouldConnectTo && !isXConnected
			|| side == Side.WEST && negXShouldConnectTo && !isZConnected
			|| side == Side.EAST && posXShouldConnectTo && !isZConnected;
		}
	}
	// Connect to both the front and back of the repeater, instead of only the back.
	@Inject(method = "shouldConnectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMetadata(III)I", ordinal = 1), cancellable = true)
	private static void connectToFrontBackRepeater(WorldSource worldSource, int x, int y, int z, int data, CallbackInfoReturnable<Boolean> cir) {
		// Get the requested side.
		Side source = BlockLogicBed.headBlockToFootBlockMap[BlockLogicBed.footToHeadMap[data]];
		// Get the side the repeater is facing.
		Side target = BlockLogicBed.headBlockToFootBlockMap[worldSource.getBlockMetadata(x, y, z) & 3];
		cir.setReturnValue(target == source || target == source.getOpposite());
	}

	// Required for setting the repeater to be a signal source.
	// Otherwise, connectToFrontBackRepeater will not be called, and redstone will redirect to all sides of the repeater.
	@Redirect(method = "shouldConnectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSignalSource()Z"))
	private static boolean dontConnectToAllRepeaterSides(Block<?> instance) {
		if(instance == Blocks.REPEATER_IDLE || instance == Blocks.REPEATER_ACTIVE) return false;
		return instance.isSignalSource();
	}
}
