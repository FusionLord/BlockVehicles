package net.fusionlord.blockvehicles.client.gui;

import net.fusionlord.blockvehicles.ModInfo;
import net.fusionlord.blockvehicles.entity.EntityVehicle;
import net.fusionlord.blockvehicles.entity.EntityVehicleSlave;
import net.fusionlord.blockvehicles.inventory.container.VehicleEditContainer;
import net.fusionlord.blockvehicles.structure.VehicleBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import java.awt.*;

/**
 * Created by FusionLord on 4/2/2016.
 */
public class GuiVehicleEdit extends GuiContainer
{
	private static final ResourceLocation guiTextures = new ResourceLocation(ModInfo.ID, "textures/gui/editor.png");
	private EntityVehicle vehicle;
	private Vec3d rotation = Vec3d.ZERO;
	private Vec3d pan = Vec3d.ZERO;
	private Rectangle viewport;
	private int lastMouseX, lastMouseY;

	private GuiButton upX, downX, upY, downY, upZ, downZ;

	public GuiVehicleEdit(EntityVehicle vehicle, VehicleEditContainer container)
	{
		super(container);
		this.vehicle = vehicle;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		viewport = new Rectangle(guiLeft + 88, guiTop + 7, xSize - 96, 70);

		buttonList.add(downX =	new GuiButton(buttonList.size(), guiLeft + 7,  guiTop + 7, 20, 20, "-"));
		buttonList.add(upX = 	new GuiButton(buttonList.size(), guiLeft + 47,  guiTop + 7, 20, 20, "+"));

		buttonList.add(downY = 	new GuiButton(buttonList.size(), guiLeft + 7,  guiTop + 30, 20, 20, "-"));
		buttonList.add(upY = 	new GuiButton(buttonList.size(), guiLeft + 47,  guiTop + 30, 20, 20, "+"));

		buttonList.add(downZ = 	new GuiButton(buttonList.size(), guiLeft + 7,  guiTop + 53, 20, 20, "-"));
		buttonList.add(upZ = 	new GuiButton(buttonList.size(), guiLeft + 47,  guiTop + 53, 20, 20, "+"));

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTextures);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		int scale = computeGuiScale();
		GL11.glScissor(viewport.getX() * scale, mc .displayHeight - (viewport.getY() + viewport.getHeight()) * scale, viewport.getWidth() * scale, viewport.getHeight() * scale);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GlStateManager.translate(viewport.getX() + viewport.getWidth() / 2, viewport.getY() + viewport.getHeight() / 2, 50);
		GlStateManager.translate(pan.xCoord, pan.yCoord, pan.zCoord);
		GlStateManager.rotate(180-45, 1, 0, 0);
		GlStateManager.rotate(-45, 0, 1, 0);
		GlStateManager.rotate(360f, (float)rotation.xCoord, (float)rotation.yCoord, (float)rotation.zCoord);
		GlStateManager.disableCull();
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(3, 3, 3);
		for(EntityVehicleSlave slave : vehicle.getSlaves())
		{
			VehicleBlock block = slave.getBlock();
			GlStateManager.pushMatrix();
//			System.out.println(block.getPos());
			GlStateManager.enableDepth();
			GlStateManager.translate(block.getPos().getX() - .5f, block.getPos().getY(), block.getPos().getZ() - .5f);
			GlStateManager.scale(1.25, 1.25, 1.25);
			RenderHelper.enableGUIStandardItemLighting();
			mc.getBlockRendererDispatcher().renderBlockBrightness(block.getState(), 15f);
			GlStateManager.popMatrix();
		}
		GlStateManager.disableDepth();
		GlStateManager.enableCull();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();

		int x = mouseX - guiLeft;
		int y = mouseY - guiTop;
		drawHorizontalLine(0, mouseX - 10, mouseY, Color.red.hashCode());
		drawHorizontalLine(mouseX + 10, width, mouseY, Color.red.hashCode());
		drawVerticalLine(mouseX, 0, mouseY - 10, Color.red.hashCode());
		drawVerticalLine(mouseX, mouseY + 10, height, Color.red.hashCode());
		int strWidth = fontRendererObj.getStringWidth(String.format("x: %s, y: %s", x, y));
		fontRendererObj.drawString(String.format("x: %s, y: %s", x, y), mouseX - strWidth / 2, mouseY + 7, Color.WHITE.hashCode());
	}

	/**
	 * <p>Computes the current GUI scale. Calling this method is equivalent to the following:<pre><code>
	 * Minecraft mc = Minecraft.getMinecraft();
	 * int scale = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).getScaleFactor();</code></pre></p>
	 *
	 * @return the current GUI scale
	 */
	public static int computeGuiScale() {
		Minecraft mc = Minecraft.getMinecraft();
		int scaleFactor = 1;

		int k = mc.gameSettings.guiScale;

		if (k == 0) {
			k = 1000;
		}

		while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}
		return scaleFactor;
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		if (viewport.contains(mouseX, mouseY) && draggedStack == null)
		{

			mouseX = mouseX - width / 2;
			mouseY = mouseY - height / 2 + guiTop + 4;

			if (clickedMouseButton == mc.thePlayer.getPrimaryHand().opposite().ordinal()) // leftClick
			{
				System.out.println(String.format("x: %s y: %s", (double)mouseX / viewport.getWidth() * 10D, (double)mouseY / viewport.getHeight() * 10D));
				pan = pan.add(new Vec3d((double)(mouseX - lastMouseX) / viewport.getWidth() * 10D, (double)(mouseY - lastMouseY) / viewport.getHeight() * 10D, 0D));
			}
			else if (clickedMouseButton == mc.thePlayer.getPrimaryHand().ordinal()) // rightClick
			{
//				double x = ((intX - width) / width);
//				double y = ((intY - height) / height);
//				System.out.println(String.format("x: %s y: %s", x, y));
//				rotation = new Vec3d(x, y, 0);
//				System.out.println(rotation);
			}

			lastMouseX = mouseX;
			lastMouseY = mouseY;
		}
	}
}
