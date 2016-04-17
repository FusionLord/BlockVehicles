package net.fusionlord.blockvehicles.proxy;

import net.fusionlord.blockvehicles.client.render.RenderVehicle;
import net.fusionlord.blockvehicles.entity.EntityVehicle;
import net.fusionlord.blockvehicles.entity.EntityVehicleSlave;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by FusionLord on 4/2/2016.
 */
public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		registerEntityRenderer(EntityVehicleSlave.class, RenderVehicle.class);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}


	//
	// The below method and class is used as part of Forge 1668+'s workaround for render manager being null during preinit
	//

	private static <E extends Entity> void registerEntityRenderer(Class<E> entityClass, Class<? extends Render<E>> renderClass)
	{
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<E>(renderClass));
	}

	private static class EntityRenderFactory<E extends Entity> implements IRenderFactory<E>
	{
		private Class<? extends Render<E>> renderClass;

		private EntityRenderFactory(Class<? extends Render<E>> renderClass)
		{
			this.renderClass = renderClass;
		}

		@Override
		public Render<E> createRenderFor(RenderManager manager)
		{
			Render<E> renderer = null;

			try
			{
				renderer = renderClass.getConstructor(RenderManager.class).newInstance(manager);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return renderer;
		}
	}
}
