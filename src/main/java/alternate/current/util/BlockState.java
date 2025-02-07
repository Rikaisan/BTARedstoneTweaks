package alternate.current.util;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockState {

	public static final BlockState AIR = new BlockState(null, 0) {
		public int getBlockId() { return 0; }
		public boolean is(Block<?> block) { return false; }
		public BlockState set(int metadata) { return this; }
		public boolean isAir() { return true; }
		public boolean isConductor() { return false; }
		public boolean isSignalSource() { return false; }
		public boolean hasSignal(World world, BlockPos pos, Direction dir) { return false; }
		public boolean hasDirectSignal(World world, BlockPos pos, Direction dir) { return false; }
		public boolean canSurvive(World world, BlockPos pos) { return true; }
		public void dropItems(World world, BlockPos pos) { }
		public void neighborChanged(World world, BlockPos pos, Block<?> fromBlock) { }
	};

	private final Block<?> block;
	private final int metadata;

	public BlockState(int blockId, int metadata) {
		this(Blocks.getBlock(blockId), metadata);
	}

	public BlockState(Block<?> block, int metadata) {
		this.block = block;
		this.metadata = metadata;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockState) {
			BlockState state = (BlockState)obj;
			return state.block == block && state.metadata == metadata;
		}

		return false;
	}

	public int getBlockId() {
		return block.id();
	}

	public Block<?> getBlock() {
		return block;
	}

	public boolean is(Block<?> block) {
		return this.block == block;
	}

	public int get() {
		return metadata;
	}

	public BlockState set(int metadata) {
		return new BlockState(block, metadata);
	}

	public boolean isAir() {
		return this == AIR;
	}

	public boolean isConductor() {
		return block.getMaterial().isSolidBlocking() && block.isCubeShaped();
	}

	public boolean isSignalSource() {
		return block.isSignalSource();
	}

	public boolean hasSignal(World world, BlockPos pos, Direction dir) {
		return block.getSignal(world, pos.x, pos.y, pos.z, Side.getSideById(dir.index));
	}

	public boolean hasDirectSignal(World world, BlockPos pos, Direction dir) {
		return block.getDirectSignal(world, pos.x, pos.y, pos.z, Side.getSideById(dir.index));
	}

	public boolean canSurvive(World world, BlockPos pos) {
		Block<?> block = world.getBlock(pos.x, pos.y, pos.z);
		return block == null || block.getMaterial().isReplaceable();
	}

	public void dropItems(World world, BlockPos pos) {
		block.dropBlockWithCause(world, EnumDropCause.WORLD, pos.x, pos.y, pos.z, 0, null, null);
	}

	public void neighborChanged(World world, BlockPos pos, Block<?> neighborBlock) {
		block.onNeighborBlockChange(world, pos.x, pos.y, pos.z, neighborBlock.id());
	}
}
