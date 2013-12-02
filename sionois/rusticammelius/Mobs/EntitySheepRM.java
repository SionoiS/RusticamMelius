package sionois.rusticammelius.Mobs;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import sionois.rusticammelius.RMItems;
import sionois.rusticammelius.AI.AIEatTallGrass;
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

	private boolean bellyFull;
	private boolean hungry;
	private boolean starving;
	
	int degreeOfDiversion = 2;

	public EntitySheepRM(World par1World)
	{
		super(par1World);
		this.setSize(0.9F, 1.3F);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.removeTask(this.aiEatGrass);
		//this.tasks.removeTask(new EntityAIMateTFC((IAnimal) super.getEntity(),worldObj, 1.0f));
		this.tasks.addTask(5, this.aiEatTallGrass);
		//this.tasks.addTask(3, new AITemptRM(this, 1.2F, false));

		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;
		
		animalID = TFC_Time.getTotalTicks() + entityId;
		pregnant = false;
		pregnancyRequiredTime = (int) (4 * TFC_Time.ticksInMonth);
		timeOfConception = 0;
		mateSizeMod = 0;
		sex = rand.nextInt(2);
		size_mod = (((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) / 100f) + 1F) * (1.0F - 0.1F * sex);

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
			System.out.println("Sheep RM");
		}
	}
	public EntitySheepRM(World par1World,IAnimal mother, float F_size)
	{
		this(par1World);
		this.posX = ((EntityLivingBase)mother).posX;
		this.posY = ((EntityLivingBase)mother).posY;
		this.posZ = ((EntityLivingBase)mother).posZ;
		size_mod = (((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) / 100f) + 1F) * (1.0F - 0.1F * sex) * (float)Math.sqrt((mother.getSize() + F_size)/1.9F);
		size_mod = Math.min(Math.max(size_mod, 0.7F),1.3f);

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
	protected void updateAITasks()
	{
		this.sheepTimer = this.aiEatTallGrass.getEatGrassTick();
		super.updateAITasks();
		
		if(!this.worldObj.isRemote)
		{
			if (this.worldObj.getTotalWorldTime() % 600L == 0L)
			{
				System.out.println("tick");
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
    	
			if (this.bellyFull && getHealth() < TFC_Core.getEntityMaxHealth(this) && !isDead)
			{
				this.heal(1);
			}
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
		return !pregnant && RMItems.BreedingFood.contains(par1ItemStack.getItem());
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
