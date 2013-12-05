package sionois.rusticammelius.Mobs;

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
import sionois.rusticammelius.RMItems;
import sionois.rusticammelius.AI.AIEatTallGrass;
import TFC.API.Entities.IAnimal;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Time;
import TFC.Entities.AI.EntityAIMateTFC;
import TFC.Entities.Mobs.EntityPigTFC;

public class EntityPigRM extends EntityPigTFC implements IFarmAnimals
{
	protected boolean bellyFull;
	protected boolean hungry;
	protected boolean starving;
	
	int degreeOfDiversion = 2;
	
	/**Number of time this animal need to eat per month*/
	protected int appetiteModifier = 20;
	
	/**Number of babies this animal can make per month*/
	protected float breedingModifier = 0.263F;

	public EntityPigRM(World par1World)
	{
		super(par1World);
		this.tasks.taskEntries.clear();
		this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));	
		this.tasks.addTask(2, new AIEatTallGrass(this, 1.2D));	
		this.tasks.addTask(3, new EntityAIMateTFC(this, worldObj, 1.0f));
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
			//System.out.println("Pig RM");
		}
	}

	public EntityPigRM(World par1World, IAnimal mother, float F_size)
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
	public void eatGrassBonus()
	{
		//System.out.println("eatGrassBonus");
		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;
	}
	@Override
	public void onLivingUpdate()
	{
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
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return !pregnant && RMItems.BreedingFood.contains(par1ItemStack.itemID);
	}
	@Override
	public EntityAgeable createChildTFC(EntityAgeable entityageable) 
	{
		return new EntityPigRM(worldObj, this, entityageable.getEntityData().getFloat("MateSize"));
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
