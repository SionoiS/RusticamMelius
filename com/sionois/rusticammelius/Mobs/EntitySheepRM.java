package com.sionois.rusticammelius.Mobs;

import com.sionois.rusticammelius.RMItems;
import com.sionois.rusticammelius.AI.AIEatTallGrass;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import TFC.TFCItems;
import TFC.API.Entities.IAnimal;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Time;
import TFC.Entities.AI.EntityAIMateTFC;
import TFC.Entities.Mobs.EntitySheepTFC;

public class EntitySheepRM extends EntitySheepTFC implements IFarmAnimals
{
	private int sheepTimer;
	
	private AIEatTallGrass aiEatTallGrass = new AIEatTallGrass(this, 1.2F);

	protected boolean bellyFull;
	protected boolean hungry;
	protected boolean starving;
	
	int degreeOfDiversion = 2;
	
	/**Number of time this animal need to eat per month*/
	protected int appetiteModifier = 50;
	
	/**Number of babies this animal can make per month*/
	protected float breedingModifier = 0.204F;

	public EntitySheepRM(World par1World)
	{
		super(par1World);
		this.tasks.taskEntries.clear();
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));    
        this.tasks.addTask(2, this.aiEatTallGrass);     
        this.tasks.addTask(3, new EntityAIMateTFC(this,worldObj, 1.0f));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
		
		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;
		
		this.animalID = TFC_Time.getTotalTicks() + entityId;
		this.pregnant = false;
		this.pregnancyRequiredTime = (int) (TFC_Time.ticksInMonth / this.breedingModifier);
		this.timeOfConception = 0;
		this.mateSizeMod = 0;
		this.sex = rand.nextInt(2);
		this.size_mod = (((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) / 100f) + 1F) * (1.0F - 0.1F * sex);

		//	We hijack the growingAge to hold the day of birth rather
		//	than number of ticks to next growth event. We want spawned
		//	animals to be adults, so we set their birthdays far enough back
		//	in time such that they reach adulthood now.
		//
		this.setAge((int) TFC_Time.getTotalDays() - getNumberOfDaysToAdult());
		//For Testing Only(makes spawned animals into babies)
		//this.setGrowingAge((int) TFC_Time.getTotalDays());
		if(!this.worldObj.isRemote)
		{
			//System.out.println("Sheep RM");
		}
	}
	public EntitySheepRM(World par1World,IAnimal mother, float F_size)
	{
		this(par1World);
		this.posX = ((EntityLivingBase)mother).posX;
		this.posY = ((EntityLivingBase)mother).posY;
		this.posZ = ((EntityLivingBase)mother).posZ;
		this.size_mod = (((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) / 100f) + 1F) * (1.0F - 0.1F * sex) * (float)Math.sqrt((mother.getSize() + F_size)/1.9F);
		this.size_mod = Math.min(Math.max(size_mod, 0.7F),1.3f);

		//	We hijack the growingAge to hold the day of birth rather
		//	than number of ticks to next growth event.
		//
		this.setAge((int) TFC_Time.getTotalDays());
	}
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	@Override
	public void onLivingUpdate()
	{
		this.sheepTimer = this.aiEatTallGrass.getEatGrassTick();
		super.onLivingUpdate();
		
        if (this.worldObj.getTotalWorldTime() % (long)(TFC_Time.ticksInMonth / (long)this.appetiteModifier) == 0L)
        {
        	//System.out.println("tick");
        	if(bellyFull)
        	{
        		this.bellyFull = false;
        		this.hungry = true;
        		this.starving = false;
        		//System.out.println("hungry");
        	}
        	else if(hungry)
        	{
        		this.bellyFull = false;
        		this.hungry = false;
        		this.starving = true;
        		//System.out.println("starving");
        	}
		}
    	if(starving)
    	{
    		this.attackEntityFrom(DamageSource.starve, 1.0F);
    	}
		if (!isDead && getHealth() < TFC_Core.getEntityMaxHealth(this) && this.bellyFull)
		{
			this.heal(1);
		}
	}

	private float getPercentGrown(IAnimal animal)
	{
		float birth = animal.getBirthDay();
		float time = (int) TFC_Time.getTotalDays();
		float percent =(time-birth)/animal.getNumberOfDaysToAdult();
		return Math.min(percent, 1f);
	}
	@Override
	public void eatGrassBonus()
	{
		System.out.println("eatGrassBonus");
		this.setSheared(false);
		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;
	}
	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return !pregnant && RMItems.BreedingFood.contains(par1ItemStack.itemID);
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("BellyFull", this.bellyFull);
		nbt.setBoolean("Hungry", this.hungry);
		nbt.setBoolean("Starving", this.starving);
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);	
		this.bellyFull = nbt.getBoolean("BellyFull");
		this.hungry = nbt.getBoolean("Hungry");
		this.starving = nbt.getBoolean("Starving");
	}
	@Override
	public EntityAgeable createChildTFC(EntityAgeable entityageable) {
		return new EntitySheepRM(worldObj, this, entityageable.getEntityData().getFloat("MateSize"));
	}
	@Override
	public boolean isHungry()
	{
		return this.hungry;
	}
	@Override
	public boolean isStarving()
	{
		return this.starving;
	}
}
