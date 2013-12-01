package sionois.rusticammelius.Mobs;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import sionois.rusticammelius.RMItems;
import sionois.rusticammelius.AI.AIEatTallGrass;
import sionois.rusticammelius.AI.AITemptRM;
import TFC.TFCItems;
import TFC.API.Entities.IAnimal;
import TFC.API.Entities.IAnimal.GenderEnum;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Time;
import TFC.Entities.AI.EntityAIMateTFC;

public class EntityCowRM extends EntityCow implements IAnimal, IFarmAnimals
{
	protected long animalID;
	protected int sex = 0;
	
	protected boolean bellyFull;
	protected boolean hungry;
	protected boolean starving;
	
	protected int hunger = 168000;
	protected long hasMilkTime;
	protected boolean pregnant;
	protected int pregnancyRequiredTime;
	protected long conception;
	protected float mateSizeMod;
	public float size_mod;
	public boolean inLove;
	int degreeOfDiversion = 1;
	
	public EntityCowRM(World par1World)
	{
		super(par1World);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(6, new AIEatTallGrass(this, 1.2F));
		this.tasks.addTask(2, new EntityAIMateTFC(this,this.worldObj, 1.0F));
		//this.tasks.addTask(3, new AITemptRM(this, 1.2F, false));
 
		this.animalID = TFC_Time.getTotalTicks() + entityId;
		
		this.bellyFull = false;
		this.hungry = true;
		this.starving = false;
		
		this.pregnant = false;
		this.pregnancyRequiredTime =(int)(4 * TFC_Time.ticksInMonth);
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
			//System.out.println("Cow RM");
		}
	}
	public EntityCowRM(World par1World, IAnimal mother, float father_size)
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
		if(super.inLove > 0){
			super.inLove = 0;
			setInLove(true);
		}

		syncData();
		if(isAdult()){
			setGrowingAge(0);
		}
		else{
			setGrowingAge(-1);
		}
		if(isPregnant()) 
		{
			if(TFC_Time.getTotalTicks() >= conception + pregnancyRequiredTime)
			{
				EntityCowRM baby = (EntityCowRM) createChildTFC(this);
				baby.setLocationAndAngles (posX+(rand.nextFloat()-0.5F)*2F,posY,posZ+(rand.nextFloat()-0.5F)*2F, 0.0F, 0.0F);
				baby.rotationYawHead = baby.rotationYaw;
				baby.renderYawOffset = baby.rotationYaw;
				worldObj.spawnEntityInWorld(baby);
				baby.setAge((int)TFC_Time.getTotalDays());
				pregnant = false;
			}
		}

		/**
		 * This Cancels out the changes made to growingAge by EntityAgeable
		 * */
		TFC_Core.PreventEntityDataUpdate = true;
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

	private float getPercentGrown(IAnimal animal)
	{
		float birth = animal.getBirthDay();
		float time = (int) TFC_Time.getTotalDays();
		float percent =(time-birth)/animal.getNumberOfDaysToAdult();
		return Math.min(percent, 1f);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(500);//MaxHealth
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger ("Sex", this.sex);
		nbt.setLong ("Animal ID", this.animalID);
		nbt.setFloat ("Size Modifier", this.size_mod);
		
		nbt.setBoolean("BellyFull", this.bellyFull);
		nbt.setBoolean("Hungry", this.hungry);
		nbt.setBoolean("Starving", this.starving);
		
		nbt.setBoolean("Pregnant", this.pregnant);
		nbt.setFloat("MateSize", this.mateSizeMod);
		nbt.setLong("ConceptionTime",this.conception);
		nbt.setInteger("Age", getBirthDay());
		nbt.setLong("HasMilkTime", this.hasMilkTime);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.animalID = nbt.getLong ("Animal ID");
		this.sex = nbt.getInteger ("Sex");
		this.size_mod = nbt.getFloat ("Size Modifier");
		this.pregnant = nbt.getBoolean("Pregnant");
		
		this.bellyFull = nbt.getBoolean("BellyFull");
		this.hungry = nbt.getBoolean("Hungry");
		this.starving = nbt.getBoolean("Starving");
		
		this.mateSizeMod = nbt.getFloat("MateSize");
		this.conception = nbt.getLong("ConceptionTime");
		this.hasMilkTime = nbt.getLong("HasMilkTime");
		this.setAge(nbt.getInteger ("Age"));
	}
	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected int getDropItemId()
	{
		return Item.leather.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		float ageMod = TFC_Core.getPercentGrown(this);
		if(isAdult())
		{
			this.dropItem(TFCItems.Hide.itemID,1);
			this.dropItem(Item.bone.itemID, rand.nextInt(6)+3);

			if (this.isBurning()) {
				this.dropItem(Item.beefCooked.itemID, (int) (ageMod*this.size_mod *(15+this.rand.nextInt(10))));
			} else {
				this.dropItem(Item.beefRaw.itemID, (int) (ageMod*this.size_mod *(15+this.rand.nextInt(10))));
			}
		}



	}


	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer player)
	{
		if(!worldObj.isRemote){
			player.addChatMessage(getGender()==GenderEnum.FEMALE?"Female":"Male");
			if(getGender()==GenderEnum.FEMALE && pregnant){
				player.addChatMessage("Pregnant");
			}
			//player.addChatMessage("12: "+dataWatcher.getWatchableObjectInt(12)+", 15: "+dataWatcher.getWatchableObjectInt(15));
		}
		if(getGender() == GenderEnum.FEMALE && isAdult() && hasMilkTime < TFC_Time.getTotalTicks())
		{
			ItemStack var2 = player.inventory.getCurrentItem();
			if (var2 != null && var2.itemID == TFCItems.WoodenBucketEmpty.itemID) 
			{
				player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(TFCItems.WoodenBucketMilk));
				hasMilkTime = TFC_Time.getTotalTicks() + TFC_Time.dayLength;//Can be milked once every day
				return true;
			}
		}

		return super.interact(player);
	}

	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return !this.pregnant && RMItems.BreedingFood.contains(par1ItemStack);
	}

	@Override
	public void setGrowingAge(int par1)
	{
		if(!TFC_Core.PreventEntityDataUpdate) {
			this.dataWatcher.updateObject(12, Integer.valueOf(par1));
		}
	}

	@Override
	public void setAge(int par1)
	{
		//if(!TFC_Core.PreventEntityDataUpdate) {
		this.dataWatcher.updateObject(15, Integer.valueOf(par1));
		//}
	}

	@Override
	public boolean isChild()
	{
		return !isAdult();
	}

	@Override
	public GenderEnum getGender() 
	{
		return GenderEnum.genders[getSex()];
	}

	@Override
	public int getSex(){
		return dataWatcher.getWatchableObjectInt(13);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) 
	{
		return null;
	}


	@Override
	public EntityAgeable createChildTFC(EntityAgeable entityageable) 
	{
		return new EntityCowRM(worldObj, this, entityageable.getEntityData().getFloat("MateSize"));
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
		return pregnant;
	}

	@Override
	public EntityLiving getEntity() 
	{
		return this;
	}

	@Override
	public boolean canMateWith(IAnimal animal) 
	{
		if(animal.getGender() != this.getGender() && animal.isAdult() && animal instanceof EntityCowRM) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public void mate(IAnimal otherAnimal) 
	{
		if (getGender() == GenderEnum.MALE)
		{
			otherAnimal.mate(this);
			return;
		}
		conception = TFC_Time.getTotalTicks();
		pregnant = true;
		resetInLove();
		otherAnimal.setInLove(false);
		mateSizeMod = otherAnimal.getSize();
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
