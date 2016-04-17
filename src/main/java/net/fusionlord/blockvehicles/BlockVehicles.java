package net.fusionlord.blockvehicles;

import net.fusionlord.blockvehicles.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(name=ModInfo.NAME, modid=ModInfo.ID, version=ModInfo.VERSION)
public class BlockVehicles
{
	@Mod.Instance(ModInfo.ID)
	public static BlockVehicles INSTANCE;

	@SidedProxy(clientSide = "net.fusionlord.blockvehicles.proxy.ClientProxy", serverSide = "net.fusionlord.blockvehicles.proxy.CommonProxy", modId = ModInfo.ID)
	public static IProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{ proxy.preInit(event); }

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{ proxy.init(event); }

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{ proxy.postInit(event); }
}
