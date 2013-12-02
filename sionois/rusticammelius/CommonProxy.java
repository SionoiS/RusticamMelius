package sionois.rusticammelius;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import sionois.rusticammelius.Mobs.EntityCowRM;
import sionois.rusticammelius.Mobs.EntityPigRM;
import sionois.rusticammelius.Mobs.EntitySheepRM;
import sionois.rusticammelius.Mobs.EntityWolfRM;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.common.registry.EntityRegistry;

public class CommonProxy
{
	public void registerRenderInformation(){}

	public void registerTileEntities()
	{
		EntityRegistry.registerGlobalEntityID(EntityChickenRM.class, "ChickenRM", ModLoader.getUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityCowRM.class, "CowRM", ModLoader.getUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityPigRM.class, "PigRM", ModLoader.getUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntitySheepRM.class, "SheepRM", ModLoader.getUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityWolfRM.class, "WolfRM", ModLoader.getUniqueEntityId(), 0xffffff, 0xaaaaaa);
	}

}
