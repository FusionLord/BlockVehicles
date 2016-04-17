package net.fusionlord.blockvehicles.network.messages;

import io.netty.buffer.ByteBuf;
import net.fusionlord.blockvehicles.entity.EntityVehicleSlave;
import net.fusionlord.blockvehicles.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageAddBlock implements IMessage
{
	private NBTTagCompound tag;

	public MessageAddBlock() {}

	public MessageAddBlock(EntityVehicleSlave entityVehicleSlave, EnumFacing facing)
	{
		tag = new NBTTagCompound();
		tag.setLong("EntityMost", entityVehicleSlave.getUniqueID().getMostSignificantBits());
		tag.setLong("EntityLeast", entityVehicleSlave.getUniqueID().getLeastSignificantBits());
		tag.setInteger("SideHit", facing.getIndex());
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class HANDLER implements IMessageHandler<MessageAddBlock, IMessage>
	{
		public HANDLER() {}

		@Override
		public IMessage onMessage(MessageAddBlock message, MessageContext ctx)
		{
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			EntityVehicleSlave vehicle = EntityUtil.getEntityByUUID(player.getEntityWorld(), new UUID(message.tag.getLong("EntityMost"), message.tag.getLong("EntityLeast")), EntityVehicleSlave.class);
			EnumFacing facing = EnumFacing.values()[message.tag.getInteger("SideHit")];
			if(vehicle != null)
			{
				vehicle.addBlock(player, facing);
			}
			return null;
		}
	}
}
