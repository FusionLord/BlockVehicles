package net.fusionlord.blockvehicles.entity;

import net.fusionlord.blockvehicles.structure.VehicleBlock;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityVehicle extends EntityLiving
{
	private List<EntityVehicleSlave> slaves;
	private Entity rider;

	public EntityVehicle(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		ignoreFrustumCheck = true;
		entityCollisionReduction = 1f;
		setSize(1.025f, 1.025f);
		setEntityInvulnerable(true);
	}

	@Override
	public EnumPushReaction getPushReaction()
	{
		return EnumPushReaction.IGNORE;
	}

	void addBlock(VehicleBlock vehicleBlock)
	{
		BlockPos pos = getPosition().add(vehicleBlock.getPos());
		EntityVehicleSlave slave = new EntityVehicleSlave(worldObj, vehicleBlock, this);
		slave.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), rotationYaw, rotationPitch);
		worldObj.spawnEntityInWorld(slave);
		if (slaves == null)
		{
			slaves = new ArrayList<>();
		}
		slaves.add(slave);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (worldObj.isRemote) return;
		if (slaves == null || slaves.isEmpty())
		{
			slaves = new ArrayList<>();
			addBlock(new VehicleBlock(new BlockPos(-1, 0, -1), Blocks.diamond_block.getDefaultState()));
			addBlock(new VehicleBlock(new BlockPos(-1, 0, 0), Blocks.diamond_block.getDefaultState()));
			addBlock(new VehicleBlock(new BlockPos(-1, 0, 1), Blocks.diamond_block.getDefaultState()));

			addBlock(new VehicleBlock(new BlockPos(0, 0, -1), Blocks.diamond_block.getDefaultState()));
			addBlock(new VehicleBlock(new BlockPos(0, 0, 0), Blocks.diamond_block.getDefaultState()));
			addBlock(new VehicleBlock(new BlockPos(0, 0, 1), Blocks.diamond_block.getDefaultState()));

			addBlock(new VehicleBlock(new BlockPos(1, 0, -1), Blocks.diamond_block.getDefaultState()));
			addBlock(new VehicleBlock(new BlockPos(1, 0, 0), Blocks.diamond_block.getDefaultState()));
			addBlock(new VehicleBlock(new BlockPos(1, 0, 1), Blocks.diamond_block.getDefaultState()));

			addBlock(new VehicleBlock(new BlockPos(0, 1, 0), Blocks.beacon.getDefaultState()));
		}
	}

	@Override
	public void setDead()
	{
		slaves.forEach(EntityVehicleSlave::setDead);
		super.setDead();
	}

	@Override
	public Entity getControllingPassenger()
	{
		return rider;
	}

	/**
	 * Moves the entity based on the specified heading.  Args: strafe, forward
	 */
	public void moveEntityWithHeading(float strafe, float forward)
	{
		if (this.isBeingRidden())
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
			this.prevRotationYaw = this.rotationYaw = entitylivingbase.rotationYaw;
			this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			forward = entitylivingbase.moveForward;

			this.stepHeight = 1.0F;
			if (entitylivingbase instanceof EntityPlayer)
			{
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}
		}
		else
		{
			this.stepHeight = 0.5F;
			super.moveEntityWithHeading(strafe, forward);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		NBTTagList blocksTag = tagCompound.getTagList("blocksTag", 10);
		for (int i = 0; i < blocksTag.tagCount(); i++)
		{
			addBlock(VehicleBlock.fromTag(blocksTag.getCompoundTagAt(i)));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		NBTTagList tagList = new NBTTagList();
		if (slaves != null && !slaves.isEmpty())
		{
			for(EntityVehicleSlave slave : slaves)
			{
				tagList.appendTag(slave.getBlock().toTag());
			}
		}
		tagCompound.setTag("blocksTag", tagList);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (!this.isBeingRidden())
		{
			this.mountTo(player);
			return true;
		}
		return super.processInitialInteract(player, stack, hand);
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
	 * pushable on contact, like boats or minecarts.
	 */
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return entityIn.getEntityBoundingBox();
	}

	/**
	 * Returns the collision bounding box for this entity
	 */
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return this.getEntityBoundingBox();
	}


	private void mountTo(EntityPlayer player)
	{
		rider = player;
		player.rotationYaw = this.rotationYaw;
		player.rotationPitch = this.rotationPitch;

		if (!this.worldObj.isRemote)
		{
			player.startRiding(this);
		}
	}

	public List<EntityVehicleSlave> getSlaves()
	{
		return slaves;
	}

	void removeSlave(EntityVehicleSlave entityVehicleSlave)
	{
		slaves.remove(entityVehicleSlave);
	}

	void addSlave(EntityVehicleSlave slave)
	{
		slaves.add(slave);
	}
}
