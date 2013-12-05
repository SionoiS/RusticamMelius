package sionois.rusticammelius.Mobs;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sionois.rusticammelius.RMItems;
import sionois.rusticammelius.AI.AITemptRM;
import TFC.API.Entities.IAnimal;
import TFC.Core.TFC_Time;
import TFC.Entities.AI.EntityAIMateTFC;
import TFC.Entities.Mobs.EntityBear;
import TFC.Entities.Mobs.EntityCowTFC;

public class EntityWildCow extends EntityCowRM implements IWildAnimals
{
	int degreeOfDiversion = 2;
	
	public EntityWildCow(World par1World)
	{
		super(par1World);
		this.tasks.taskEntries.clear();
		this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMateTFC(this,worldObj, 1.0f));
        this.tasks.addTask(2, new AITemptRM(this, 0.25D, true));
    	this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityPlayer.class, 35.0F, 1.5D, 2.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolfRM.class, 25f, 1.5D, 2.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityBear.class, 25f, 1.5D, 2.0D));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAILookIdle(this));
		
		this.animalID = TFC_Time.getTotalTicks() + entityId;
		this.pregnant = false;
		this.pregnancyRequiredTime = (int) (TFC_Time.ticksInMonth / this.breedingModifier);
		this.conception = 0;
		this.mateSizeMod = 0;
		this.sex = rand.nextInt(2);
		this.size_mod =(float)Math.sqrt((((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) * 0.01f) + 1F) * (1.0F - 0.1F * sex));
		
		//	We hijack the growingAge to hold the day of birth rather
		//	than number of ticks to next growth event. We want spawned
		//	animals to be adults, so we set their birthdays far enough back
		//	in time such that they reach adulthood now.
		//
		//this.setGrowingAge((int) TFC_Time.getTotalDays() - getNumberOfDaysToAdult());
		this.setAge((int) TFC_Time.getTotalDays() - getNumberOfDaysToAdult());
		//For Testing Only(makes spawned animals into babies)
		//this.setGrowingAge((int) TFC_Time.getTotalDays());
		if(!this.worldObj.isRemote)
		{
			//System.out.println("Wild Cow");
		}
	}
	public EntityWildCow(World par1World, IAnimal mother, float father_size)
	{
		this(par1World);
		this.posX = ((EntityLivingBase)mother).posX;
		this.posY = ((EntityLivingBase)mother).posY;
		this.posZ = ((EntityLivingBase)mother).posZ;
		this.size_mod = (float)Math.sqrt((((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) / 100f) + 1F) * (1.0F - 0.1F * sex) * (float)Math.sqrt((mother.getSize() + father_size)/1.95F));
		this.size_mod = Math.min(Math.max(size_mod, 0.7F),1.3f);

		//	We hijack the growingAge to hold the day of birth rather
		//	than number of ticks to next growth event.
		//
		this.setAge((int) TFC_Time.getTotalDays());
	}
	private float getPercentGrown(IAnimal animal)
	{
		float birth = animal.getBirthDay();
		float time = (int) TFC_Time.getTotalDays();
		float percent =(time-birth)/animal.getNumberOfDaysToAdult();
		return Math.min(percent, 1f);
	}
	@Override
	public void onLivingUpdate()
	{	
		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;
		super.onLivingUpdate();
	}
}
