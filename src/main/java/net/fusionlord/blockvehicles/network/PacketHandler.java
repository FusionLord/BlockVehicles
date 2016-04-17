package net.fusionlord.blockvehicles.network;

import net.fusionlord.blockvehicles.ModInfo;
import net.fusionlord.blockvehicles.network.messages.MessageAddBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by FusionLord on 4/10/2016.
 */
public class PacketHandler
{
	private static SimpleNetworkWrapper INSTANCE;
	int messageCount = 0;

	public PacketHandler()
	{
		INSTANCE = new SimpleNetworkWrapper(ModInfo.ID);
		INSTANCE.registerMessage(MessageAddBlock.HANDLER.class, MessageAddBlock.class, 0, Side.SERVER);
	}

	public static void sendTo(IMessage message, EntityPlayerMP player)
	{
		INSTANCE.sendTo(message, player);
	}

	public static void sendToAll(IMessage message)
	{
		INSTANCE.sendToAll(message);
	}

	public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		INSTANCE.sendToAllAround(message, point);
	}

	public static void sendToDimension(IMessage message, int dimensionId)
	{
		INSTANCE.sendToDimension(message, dimensionId);
	}

	public static void sendToServer(IMessage message)
	{
		INSTANCE.sendToServer(message);
	}
}
