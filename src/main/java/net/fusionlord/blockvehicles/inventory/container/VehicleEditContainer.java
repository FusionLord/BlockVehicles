package net.fusionlord.blockvehicles.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by FusionLord on 4/3/2016.
 */
public class VehicleEditContainer extends Container
{
	public VehicleEditContainer(IInventory inventory, InventoryPlayer playerInventory)
	{
		super();
		addSlotToContainer(new Slot(inventory, 0, 8, 7));
		bindPlayerInventory(playerInventory);
	}

	private void bindPlayerInventory(InventoryPlayer playerInventory)
	{
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}
