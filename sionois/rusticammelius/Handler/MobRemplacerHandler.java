package sionois.rusticammelius.Handler;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import sionois.rusticammelius.Mobs.EntityCowRM;
import sionois.rusticammelius.Mobs.EntityPigRM;
import sionois.rusticammelius.Mobs.EntitySheepRM;
import sionois.rusticammelius.Mobs.IFarmAnimals;
import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.Entities.Mobs.EntityCowTFC;
import TFC.Entities.Mobs.EntityPigTFC;
import TFC.Entities.Mobs.EntitySheepTFC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class MobRemplacerHandler 
{
	@ForgeSubscribe
	public void onEntityLivingSpawn(LivingSpawnEvent event)
	{
        if (!event.world.isRemote)
        {
            if ((event.entityLiving instanceof EntityCowTFC) && !(event.entityLiving instanceof IFarmAnimals))
            {
            	EntityCowTFC cowTFC = (EntityCowTFC) event.entityLiving;
            	EntityCowRM cowRM = new EntityCowRM(event.world);
            	cowRM.setLocationAndAngles(cowTFC.posX, cowTFC.posY, cowTFC.posZ, cowTFC.rotationYaw, cowTFC.rotationPitch);
            	event.world.spawnEntityInWorld(cowRM);
            	cowTFC.setDead();
            	//System.out.println("Cow Remplaced");
            }
            else if ((event.entityLiving instanceof EntitySheepTFC) && !(event.entityLiving instanceof IFarmAnimals))
            {
            	EntitySheepTFC sheepTFC = (EntitySheepTFC) event.entityLiving;
            	EntitySheepRM sheepRM = new EntitySheepRM(event.world);
            	sheepRM.setLocationAndAngles(sheepTFC.posX, sheepTFC.posY, sheepTFC.posZ, sheepTFC.rotationYaw, sheepTFC.rotationPitch);
            	event.world.spawnEntityInWorld(sheepRM);
            	sheepTFC.setDead();
            	//System.out.println("Sheep Remplaced");
            }
            else if ((event.entityLiving instanceof EntityPigTFC) && !(event.entityLiving instanceof IFarmAnimals))
            {
            	EntityPigTFC pigTFC = (EntityPigTFC) event.entityLiving;
            	EntityPigRM pigRM = new EntityPigRM(event.world);
            	pigRM.setLocationAndAngles(pigTFC.posX, pigTFC.posY, pigTFC.posZ, pigTFC.rotationYaw, pigTFC.rotationPitch);
            	event.world.spawnEntityInWorld(pigRM);
            	pigTFC.setDead();
            	//System.out.println("Pig Remplaced");
            }
            else if ((event.entityLiving instanceof EntityChickenTFC) && !(event.entityLiving instanceof IFarmAnimals))
            {
            	EntityChickenTFC chickenTFC = (EntityChickenTFC) event.entityLiving;
            	EntityChickenRM chickenRM = new EntityChickenRM(event.world);
            	chickenRM.setLocationAndAngles(chickenTFC.posX, chickenTFC.posY, chickenTFC.posZ, chickenTFC.rotationYaw, chickenTFC.rotationPitch);
            	event.world.spawnEntityInWorld(chickenRM);
            	chickenTFC.setDead();
            	//System.out.println("Chicken Remplaced");
            }
        }
	}
}
