package com.rikaisan.mixin.block.logic;

import com.rikaisan.AdditionalDoorTypeLogic;
import com.rikaisan.RedstoneTweaks;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockLogicFenceGate.class, remap = false)
public class BlockLogicFenceGateMixin extends BlockLogic {
	public BlockLogicFenceGateMixin(Block<?> block, Material material) { super(block, material); }

	@Redirect(method = "onBlockPlacedOnSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockMetadataWithNotify(IIII)V"))
	private void checkPowerOnBlockPlacedOnSide(World world, int x, int y, int z, int meta) {
		int metaWithPower = AdditionalDoorTypeLogic.setOpen(meta, world.hasNeighborSignal(x, y, z));
		world.setBlockMetadataWithNotify(x, y, z, metaWithPower);
	}

	@Redirect(method = "onBlockPlacedByMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockMetadataWithNotify(IIII)V"))
	private void checkPowerOnBlockPlacedByMob(World world, int x, int y, int z, int meta) {
		int metaWithPower = AdditionalDoorTypeLogic.setOpen(meta, world.hasNeighborSignal(x, y, z));
		world.setBlockMetadataWithNotify(x, y, z, metaWithPower);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (!world.isClientSide) {
			Block<?> block = Blocks.getBlock(blockId);
			if (block != null && block.isSignalSource()) {
				int meta = world.getBlockMetadata(x, y, z);
				boolean isOpen = AdditionalDoorTypeLogic.isOpen(meta);
				boolean isPowered = world.hasNeighborSignal(x, y, z);
				RedstoneTweaks.LOGGER.info("open: {}, powered: {}", isOpen, isPowered);
				if (isOpen != isPowered) {
					int metaWithPower = AdditionalDoorTypeLogic.setOpen(meta, isPowered);
					world.setBlockMetadataWithNotify(x, y, z, metaWithPower);
					if (Math.random() < 0.5) {
						world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, (double)x, (double)y, (double)z, "random.door_open", 1.0F, 1.0F);
					} else {
						world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, (double)x, (double)y, (double)z, "random.door_close", 1.0F, 1.0F);
					}
				}
			}
		}
	}
}
