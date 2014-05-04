package com.sionois.rusticammelius;

import com.sionois.rusticammelius.Mobs.EntityChickenRM;
import com.sionois.rusticammelius.Mobs.EntityCowRM;
import com.sionois.rusticammelius.Mobs.EntityPigRM;
import com.sionois.rusticammelius.Mobs.EntitySheepRM;
import com.sionois.rusticammelius.Mobs.EntityWildChicken;
import com.sionois.rusticammelius.Mobs.EntityWildCow;
import com.sionois.rusticammelius.Mobs.EntityWildPig;
import com.sionois.rusticammelius.Mobs.EntityWildSheep;
import com.sionois.rusticammelius.Mobs.EntityWolfRM;

import TFC.TerraFirmaCraft;
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
