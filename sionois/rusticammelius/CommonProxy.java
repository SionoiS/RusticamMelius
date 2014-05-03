package sionois.rusticammelius;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import sionois.rusticammelius.Mobs.EntityCowRM;
import sionois.rusticammelius.Mobs.EntityPigRM;
import sionois.rusticammelius.Mobs.EntitySheepRM;
import sionois.rusticammelius.Mobs.EntityWildChicken;
import sionois.rusticammelius.Mobs.EntityWildCow;
import sionois.rusticammelius.Mobs.EntityWildPig;
import sionois.rusticammelius.Mobs.EntityWildSheep;
import sionois.rusticammelius.Mobs.EntityWolfRM;
import cpw.mods.fml.common.registry.EntityRegistry;

public class CommonProxy
{
	public void registerRenderInformation(){}

	public void registerTileEntities()
	{
		EntityRegistry.registerGlobalEntityID(EntityChickenRM.class, "ChickenRM", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityCowRM.class, "CowRM", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityPigRM.class, "PigRM", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntitySheepRM.class, "SheepRM", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityWolfRM.class, "WolfRM", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		
		EntityRegistry.registerGlobalEntityID(EntityWildChicken.class, "WildChicken", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityWildCow.class, "WildCow", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityWildPig.class, "WildPig", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
		EntityRegistry.registerGlobalEntityID(EntityWildSheep.class, "WildSheep", EntityRegistry.findGlobalUniqueEntityId(), 0xffffff, 0xaaaaaa);
	}

}
