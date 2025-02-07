package alternate.current.wire;

import alternate.current.util.BlockPos;
import alternate.current.util.BlockState;
import net.minecraft.core.world.World;

public class WorldHelper {

	private static final int Y_MIN = 0;

	static BlockState getBlockState(World world, BlockPos pos) {
		if (pos.y < Y_MIN || pos.y >= world.getHeightBlocks()) {
			return BlockState.AIR;
		}

		int blockId = world.getBlockId(pos.x, pos.y, pos.z);

		if (blockId == 0) {
			return BlockState.AIR;
		}

		return new BlockState(blockId, world.getBlockMetadata(pos.x, pos.y, pos.z));
	}

	/**
	 * An optimized version of {@link net.minecraft.world.World#setBlockState
	 * World.setBlockState}. Since this method is only used to update redstone wire block
	 * states, lighting checks, height map updates, and block entity updates are
	 * omitted.
	 */
	static boolean setWireState(World world, BlockPos pos, BlockState state) {
		if (pos.y < Y_MIN || pos.y >= world.getHeightBlocks()) {
			return false;
		}

		int blockId = state.getBlockId();
		int prevBlockId = world.getBlockId(pos.x, pos.y, pos.z);

		if (blockId != prevBlockId) {
			return false;
		}

		int metadata = state.get();
		int prevMetadata = world.getBlockMetadata(pos.x, pos.y, pos.z);

		if (metadata == prevMetadata) {
			return false;
		}

		world.setBlockMetadata(pos.x, pos.y, pos.z, metadata);

		// notify clients of the BlockState change
		world.notifyBlockChange(pos.x, pos.y, pos.z, blockId);
		// mark the chunk for saving
		world.markBlockDirty(pos.x, pos.y, pos.z);

		return true;
	}
}
