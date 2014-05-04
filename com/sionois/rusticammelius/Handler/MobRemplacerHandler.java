package com.sionois.rusticammelius.Handler;

import com.sionois.rusticammelius.Mobs.EntityWildChicken;
import com.sionois.rusticammelius.Mobs.EntityWildCow;
import com.sionois.rusticammelius.Mobs.EntityWildPig;
import com.sionois.rusticammelius.Mobs.EntityWildSheep;
import com.sionois.rusticammelius.Mobs.EntityWolfRM;
import com.sionois.rusticammelius.Mobs.IFarmAnimals;
import com.sionois.rusticammelius.Mobs.IWildAnimals;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.Entities.Mobs.EntityCowTFC;
import TFC.Entities.Mobs.EntityPigTFC;
import TFC.Entities.Mobs.EntitySheepTFC;
import TFC.Entities.Mobs.EntityWolfTFC;

public class MobRemplacerHandler 
{
	@ForgeSubscribe
	public void onEntityLivingSpawn(LivingSpawnEvent event)
	{
        if (!event.world.isRemote)
        {
        	if ((event.entityLiving instanceof EntityChickenTFC) && !(event.entityLiving instanceof IFarmAnimals) && !(event.entityLiving instanceof IWildAnimals))
            {
            	EntityChickenTFC chickenTFC = (EntityChickenTFC) event.entityLiving;
            	EntityWildChicken chicken = new EntityWildChicken(event.world);
            	chicken.setLocationAndAngles(chickenTFC.posX, chickenTFC.posY, chickenTFC.posZ, chickenTFC.rotationYaw, chickenTFC.rotationPitch);
            	event.world.spawnEntityInWorld(chicken);
            	chickenTFC.setDead();
            	//System.out.println("Chicken Remplaced");
            }
            if ((event.entityLiving instanceof EntityCowTFC) && !(event.entityLiving instanceof IFarmAnimals) && !(event.entityLiving instanceof IWildAnimals))
            {
            	EntityCowTFC cowTFC = (EntityCowTFC) event.entityLiving;
            	EntityWildCow cow = new EntityWildCow(event.world);
            	cow.setLocationAndAngles(cowTFC.posX, cowTFC.posY, cowTFC.posZ, cowTFC.rotationYaw, cowTFC.rotationPitch);
            	event.world.spawnEntityInWorld(cow);
            	cowTFC.setDead();
            	//System.out.println("Cow Remplaced");
            }
            else if ((event.entityLiving instanceof EntityPigTFC) && !(event.entityLiving instanceof IFarmAnimals) && !(event.entityLiving instanceof IWildAnimals))
            {
            	EntityPigTFC pigTFC = (EntityPigTFC) event.entityLiving;
            	EntityWildPig pig = new EntityWildPig(event.world);
            	pig.setLocationAndAngles(pigTFC.posX, pigTFC.posY, pigTFC.posZ, pigTFC.rotationYaw, pigTFC.rotationPitch);
            	event.world.spawnEntityInWorld(pig);
            	pigTFC.setDead();
            	//System.out.println("Pig Remplaced");
            }
            else if ((event.entityLiving instanceof EntitySheepTFC) && !(event.entityLiving instanceof IFarmAnimals) && !(event.entityLiving instanceof IWildAnimals))
            {
            	EntitySheepTFC sheepTFC = (EntitySheepTFC) event.entityLiving;
            	EntityWildSheep sheep = new EntityWildSheep(event.world);
            	sheep.setLocationAndAngles(sheepTFC.posX, sheepTFC.posY, sheepTFC.posZ, sheepTFC.rotationYaw, sheepTFC.rotationPitch);
            	event.world.spawnEntityInWorld(sheep);
            	sheepTFC.setDead();
            	//System.out.println("Sheep Remplaced");
            }
            else if ((event.entityLiving instanceof EntityWolfTFC) && !(event.entityLiving instanceof IFarmAnimals))
            {
            	EntityWolfTFC wolfTFC = (EntityWolfTFC) event.entityLiving;
            	EntityWolfRM wolf = new EntityWolfRM(event.world);
            	wolf.setLocationAndAngles(wolfTFC.posX, wolfTFC.posY, wolfTFC.posZ, wolfTFC.rotationYaw, wolfTFC.rotationPitch);
            	event.world.spawnEntityInWorld(wolf);
            	wolfTFC.setDead();
            	//System.out.println("Wolf Remplaced");
            }
        }
	}
}
