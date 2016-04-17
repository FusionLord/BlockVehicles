package net.fusionlord.blockvehicles.entity;

import net.fusionlord.blockvehicles.network.PacketHandler;
import net.fusionlord.blockvehicles.network.messages.MessageAddBlock;
import net.fusionlord.blockvehicles.structure.VehicleBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Created by FusionLord on 4/10/2016.
 */
public class EntityVehicleSlave extends Entity
{
	private VehicleBlock block;
	private EntityVehicle master;

	public EntityVehicleSlave(World world) { super(world); }

	EntityVehicleSlave(World worldIn, VehicleBlock block, EntityVehicle master)
	{
		super(worldIn);
		this.block = block;
		this.master = master;
	}

	@Override
	protected void entityInit()
	{
		setSize(1f, 1f);
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}

	@Override
	public boolean hitByEntity(Entity entityIn)
	{
		if (!(entityIn instanceof EntityPlayer) || block == null) return super.hitByEntity(entityIn);
		if (!worldObj.isRemote)
		{
			ItemStack stack = new ItemStack(block.getState().getBlock().getItemDropped(block.getState(), worldObj.rand, 0), 1, block.getState().getBlock().damageDropped(block.getState()));
			EntityItem item = new EntityItem(worldObj, posX, posY, posZ, stack);
			worldObj.spawnEntityInWorld(item);
		}
		master.removeSlave(this);
		setDead();
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(block.getState().getBlock(), 1, block.getState().getBlock().getMetaFromState(block.getState()));
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand)
	{
		if (!worldObj.isRemote)
		{
			return super.processInitialInteract(player, stack, hand);
		}
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		Vec3d vec3d = player.getPositionEyes(partialTicks);
		Vec3d vec3d1 = player.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * 5D, vec3d1.yCoord * 5D, vec3d1.zCoord * 5D);
		RayTraceResult target = rayTrace(getPosition(), vec3d, vec3d2, getEntityBoundingBox());
		if (target != null)
		{
			System.out.println(target.sideHit.name());
			if (stack != null && stack.getItem() instanceof ItemBlock)
			{
				PacketHandler.sendToServer(new MessageAddBlock(this, target.sideHit));
				return true;
			}
		}
		return false;
	}

	private RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox)
	{
		Vec3d vec3d = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
		Vec3d vec3d1 = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
		RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
		return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		worldObj.loadedEntityList.forEach(entity -> master = entity.getUniqueID().equals(new UUID(tagCompund.getLong("masterMost"), tagCompund.getLong("masterLeast"))) ? (EntityVehicle) entity : null);
		if (master == null) setDead();
		master.addSlave(this);
		block = new VehicleBlock(BlockPos.fromLong(tagCompund.getLong("blockPos")), Block.getBlockById(tagCompund.getInteger("blockID")).getStateFromMeta(tagCompund.getInteger("blockMeta")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setLong("masterMost", master.getUniqueID().getMostSignificantBits());
		tagCompound.setLong("masterLeast", master.getUniqueID().getLeastSignificantBits());
		tagCompound.setInteger("blockID", Block.getIdFromBlock(block.getState().getBlock()));
		tagCompound.setInteger("blockMeta", block.getState().getBlock().getMetaFromState(block.getState()));
		tagCompound.setLong("blockPos", block.getPos().toLong());
	}

	public VehicleBlock getBlock()
	{
		return block;
	}

	public void addBlock(EntityPlayer player, EnumFacing facing)
	{
		Block block = Block.getBlockFromItem(player.getHeldItemMainhand().getItem());

		master.addBlock(new VehicleBlock(this.block.getPos().offset(facing), block.getDefaultState()));
	}


}
