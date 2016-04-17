package net.fusionlord.blockvehicles.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by FusionLord on 4/2/2016.
 */
public class VehicleBlock
{
	private BlockPos pos;
	private IBlockState state;

	public VehicleBlock(BlockPos pos, IBlockState state)
	{
		this.pos = pos;
		this.state = state;
	}

	public BlockPos getPos()
	{
		return pos;
	}

	public IBlockState getState()
	{
		return state;
	}

	public static VehicleBlock fromTag(NBTTagCompound tag)
	{
		Block block = Block.blockRegistry.getObjectById(tag.getInteger("blockID"));
		return new VehicleBlock(BlockPos.fromLong(tag.getLong("blockPos")), block.getStateFromMeta(tag.getInteger("blockMeta")));
	}

	public NBTTagCompound toTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("blockID", Block.blockRegistry.getIDForObject(state.getBlock()));
		tag.setInteger("blockMeta", state.getBlock().getMetaFromState(state));
		tag.setFloat("blockPos", pos.toLong());
		return tag;
	}
}
