package net.fusionlord.blockvehicles.proxy;

import net.fusionlord.blockvehicles.BlockVehicles;
import net.fusionlord.blockvehicles.entity.EntityVehicle;
import net.fusionlord.blockvehicles.entity.EntityVehicleSlave;
import net.fusionlord.blockvehicles.handler.GuiHandler;
import net.fusionlord.blockvehicles.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by FusionLord on 4/2/2016.
 */
public class CommonProxy implements IProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		new PacketHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(BlockVehicles.INSTANCE, new GuiHandler());
		EntityRegistry.registerModEntity(EntityVehicle.class, "entityVehicle", 0, BlockVehicles.INSTANCE, 64, 1, false);
		EntityRegistry.registerModEntity(EntityVehicleSlave.class, "entityVehicleSlave", 1, BlockVehicles.INSTANCE, 64, 1, false);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{

	}
}
