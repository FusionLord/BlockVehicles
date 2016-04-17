package net.fusionlord.blockvehicles.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityUtil
{
	public static <T extends Entity> T getEntityByUUID(World world, UUID uuid, Class<T> tClass)
	{
		Entity entity = null;
		for (Entity e : world.getLoadedEntityList())
		{
			LogHelper.info(String.format("matching %s to %s", uuid, e.getUniqueID()));
			if(e.getUniqueID().equals(uuid))
			{
				entity = e;
			}
		}
		if (tClass.isInstance(entity)) return tClass.cast(entity);
		return null;
	}
}
