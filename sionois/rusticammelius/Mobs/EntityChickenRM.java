package sionois.rusticammelius.Mobs;

import sionois.rusticammelius.RMItems;
import sionois.rusticammelius.AI.AIEatTallGrass;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import TFC.API.Entities.IAnimal;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Time;
import TFC.Entities.Mobs.EntityChickenTFC;

public class EntityChickenRM extends EntityChickenTFC implements IFarmAnimals
{
	private boolean bellyFull;
	private boolean hungry;
	private boolean starving;

	int degreeOfDiversion = 2;
	
	public EntityChickenRM(World par1World)
	{
		super(par1World);
		this.setSize(0.3F, 0.7F);
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 24000;
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(6, new AIEatTallGrass(this, 1.2F));
		//this.tasks.addTask(3, new EntityAIMateTFC(this,this.worldObj, 1.0F));
		//this.tasks.addTask(3, new AITemptRM(this, 1.2F, false));

		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;

		animalID = TFC_Time.getTotalTicks() + entityId;
		mateSizeMod = 1f;
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
			//System.out.println("Chicken RM");
		}
	}
	public EntityChickenRM(World world, IAnimal mother, float f_size)
	{
		this(world);
		this.posX = ((EntityLivingBase)mother).posX;
		this.posY = ((EntityLivingBase)mother).posY;
		this.posZ = ((EntityLivingBase)mother).posZ;
		size_mod = (((rand.nextInt (degreeOfDiversion+1)*10*(rand.nextBoolean()?1:-1)) / 100f) + 1F) * (1.0F - 0.1F * sex) * (float)Math.sqrt((mother.getSize() + f_size)/1.9F);
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
	public void eatGrassBonus()
	{
		//System.out.println("eatGrassBonus");
		this.bellyFull = true;
		this.hungry = false;
		this.starving = false;
	}
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		
        if (this.worldObj.getTotalWorldTime() % 24000L == 0L)
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
		if (this.bellyFull && getHealth() < TFC_Core.getEntityMaxHealth(this) && !isDead)
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
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && RMItems.BreedingFood.contains(par1ItemStack.getItem());
    }
	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) 
	{
		return new EntityChickenRM(worldObj, this, mateSizeMod);
	}

	@Override
	public EntityAgeable createChildTFC(EntityAgeable entityageable) {
		return new EntityChickenRM(worldObj, this, entityageable.getEntityData().getFloat("MateSize"));
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
