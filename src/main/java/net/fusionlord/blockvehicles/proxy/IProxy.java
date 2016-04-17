package net.fusionlord.blockvehicles.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by FusionLord on 4/2/2016.
 */
public interface IProxy
{
	void preInit(FMLPreInitializationEvent event);
	void init(FMLInitializationEvent event);
	void postInit(FMLPostInitializationEvent event);
}
