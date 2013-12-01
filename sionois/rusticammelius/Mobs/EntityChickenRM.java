package sionois.rusticammelius.Mobs;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import sionois.rusticammelius.RMItems;
import sionois.rusticammelius.AI.AIEatTallGrass;
import sionois.rusticammelius.AI.AITemptRM;
import TFC.API.Entities.IAnimal;
import TFC.API.Entities.IAnimal.GenderEnum;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Time;
import TFC.Entities.AI.EntityAIMateTFC;

public class EntityChickenRM extends EntityChicken implements IAnimal, IFarmAnimals
{
	protected long animalID;
	protected int sex;
	protected boolean bellyFull;
	protected boolean hungry;
	protected boolean starving;
	protected int hunger = 168000;
	protected long hasMilkTime;
	protected int age;
	protected float mateSizeMod;
	public float size_mod;
	public boolean inLove;
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

		this.bellyFull = false;
		this.hungry = true;
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
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(13, Integer.valueOf(0));
		this.dataWatcher.addObject(14, Float.valueOf(1.0f));
		this.dataWatcher.addObject(15, Integer.valueOf(0));
	}
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(200);//MaxHealth
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
		syncData();
		if(isAdult()){
			setGrowingAge(0);
		}
		else{
			setGrowingAge(-1);
		}

		if (isAdult() && getGender() == GenderEnum.FEMALE && !this.worldObj.isRemote && --this.timeUntilNextEgg == 0)
		{
			this.worldObj.playSoundAtEntity(this, "mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Item.egg.itemID, 1);
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 24000;
		}
		/**
		 * This Cancels out the changes made to growingAge by EntityAgeable
		 * */
		TFC_Core.PreventEntityDataUpdate = true;
		timeUntilNextEgg=10000;
		super.onLivingUpdate();
		TFC_Core.PreventEntityDataUpdate = false;

		if (this.bellyFull && getHealth() < TFC_Core.getEntityMaxHealth(this) && !isDead)
		{
			this.heal(1);
		}
	}

	public void syncData()
	{
		if(dataWatcher!= null)
		{
			if(!this.worldObj.isRemote){
				this.dataWatcher.updateObject(13, Integer.valueOf(sex));
				this.dataWatcher.updateObject(14, Float.valueOf(size_mod));
			}
			else{
				sex = this.dataWatcher.getWatchableObjectInt(13);
				size_mod = this.dataWatcher.getWatchableObjectFloat(14);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger ("Sex", sex);
		
		nbt.setBoolean("BellyFull", this.bellyFull);
		nbt.setBoolean("Hungry", this.hungry);
		nbt.setBoolean("Starving", this.starving);
		
		nbt.setLong ("Animal ID", animalID);
		nbt.setFloat ("Size Modifier", size_mod);
		nbt.setFloat("MateSize", mateSizeMod);
		nbt.setInteger("Age", getBirthDay());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		animalID = nbt.getLong ("Animal ID");
		sex = nbt.getInteger ("Sex");
		
		this.bellyFull = nbt.getBoolean("BellyFull");
		this.hungry = nbt.getBoolean("Hungry");
		this.starving = nbt.getBoolean("Starving");
		
		size_mod = nbt.getFloat ("Size Modifier");
		mateSizeMod = nbt.getFloat("MateSize");
		this.dataWatcher.updateObject(15, nbt.getInteger ("Age"));
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected int getDropItemId()
	{
		return Item.feather.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		float ageMod = TFC_Core.getPercentGrown(this);
		this.dropItem(Item.feather.itemID,(int) (ageMod * this.size_mod * (5+this.rand.nextInt(10))));

		if(isAdult())
		{
			if (this.isBurning())
			{
				this.dropItem(Item.chickenCooked.itemID, 1);
			}
			else
			{
				this.dropItem(Item.chickenRaw.itemID, 1);
			}
			this.dropItem(Item.bone.itemID, rand.nextInt(2)+1);
		}
	}

	@Override
	public void setGrowingAge(int par1)
	{
		if(!TFC_Core.PreventEntityDataUpdate) {
			this.dataWatcher.updateObject(12, Integer.valueOf(par1));
		}
	}

	@Override
	public boolean isChild()
	{
		return !isAdult();
	}
    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && RMItems.BreedingFood.contains(par1ItemStack);
    }
	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) 
	{
		return new EntityChickenRM(worldObj, this, mateSizeMod);
	}

	@Override
	public int getBirthDay() 
	{
		return this.dataWatcher.getWatchableObjectInt(15);
	}

	@Override
	public int getNumberOfDaysToAdult() 
	{
		return TFC_Time.daysInMonth * 3;
	}

	@Override
	public boolean isAdult() 
	{
		return getBirthDay()+getNumberOfDaysToAdult() <= TFC_Time.getTotalDays();
	}

	@Override
	public float getSize() 
	{
		return size_mod;
	}

	@Override
	public boolean isPregnant() 
	{
		return false;
	}

	@Override
	public EntityLiving getEntity() 
	{
		return this;
	}

	@Override
	public boolean canMateWith(IAnimal animal) 
	{
		if(animal.getGender() != this.getGender() && animal.isAdult() && animal instanceof EntityChickenRM) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void mate(IAnimal otherAnimal) 
	{

	}

	@Override
	public boolean getInLove()
	{
		return inLove;
	}

	@Override
	public void setInLove(boolean b) 
	{
		this.inLove = b;
	}

	@Override
	public long getAnimalID() 
	{
		return animalID;
	}

	@Override
	public void setAnimalID(long id) 
	{
		animalID = id;
	}

	@Override
	public int getHunger() {
		return hunger;
	}

	@Override
	public void setHunger(int h) 
	{
		hunger = h;
	}
	@Override
	public GenderEnum getGender() 
	{
		return GenderEnum.genders[getSex()];
	}
	@Override
	public int getSex() {
		return dataWatcher.getWatchableObjectInt(13);
	}
	@Override
	public EntityAgeable createChildTFC(EntityAgeable entityageable) {
		return new EntityChickenRM(worldObj, this, entityageable.getEntityData().getFloat("MateSize"));
	}
	@Override
	public void setAge(int par1) {
		this.dataWatcher.updateObject(15, Integer.valueOf(par1));
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		if(!worldObj.isRemote){
			par1EntityPlayer.addChatMessage(getGender()==GenderEnum.FEMALE?"Female":"Male");
			//if(getGender()==GenderEnum.FEMALE && pregnant){
			//	par1EntityPlayer.addChatMessage("Pregnant");
			//}
			//par1EntityPlayer.addChatMessage("12: "+dataWatcher.getWatchableObjectInt(12)+", 15: "+dataWatcher.getWatchableObjectInt(15));
		}
		return super.interact(par1EntityPlayer);
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
