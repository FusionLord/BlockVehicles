package net.fusionlord.blockvehicles.client.render;

import net.fusionlord.blockvehicles.entity.EntityVehicleSlave;
import net.fusionlord.blockvehicles.structure.VehicleBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by FusionLord on 4/2/2016.
 */
public class RenderVehicle extends Render<EntityVehicleSlave>
{
	public RenderVehicle(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityVehicleSlave vehicle, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (vehicle.getBlock() == null) return;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x - .5f, y, z + .5f);
		doRender(vehicle, partialTicks);
		GlStateManager.popMatrix();
	}

	private void doRender(EntityVehicleSlave vehicle, float partialTicks)
	{
		Minecraft mc = Minecraft.getMinecraft();
		VehicleBlock block = vehicle.getBlock();
		GlStateManager.pushMatrix();
		mc.getBlockRendererDispatcher().renderBlockBrightness(block.getState(), 15f);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVehicleSlave entity)
	{
		return null;
	}
}
