package sionois.rusticammelius.Mobs;

import sionois.rusticammelius.RMItems;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import TFC.TFCItems;
import TFC.API.Entities.IAnimal;
import TFC.API.Entities.IAnimal.GenderEnum;
import TFC.Core.TFC_MobData;
import TFC.Core.TFC_Time;
import TFC.Entities.AI.EntityAIMateTFC;
import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.Entities.Mobs.EntityCowTFC;
import TFC.Entities.Mobs.EntityDeer;
import TFC.Entities.Mobs.EntityPigTFC;
import TFC.Entities.Mobs.EntitySheepTFC;
import TFC.Entities.Mobs.EntityWolfTFC;

public class EntityWolfRM extends EntityWolfTFC implements IFarmAnimals
{
	private int degreeofdiversion = 1;
	
	public EntityWolfRM(World par1World)
	{
		super(par1World);
		this.tasks.taskEntries.clear();
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMateTFC(this, worldObj, 1));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheepRM.class, 200, false));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityChickenRM.class, 200, false));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityPigRM.class, 200, false));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityDeer.class, 200, false));
        this.setTamed(false);

		hunger = 168000;
		animalID = TFC_Time.getTotalTicks() + entityId;
		pregnant = false;
		pregnancyRequiredTime = (int) (4 * TFC_Time.ticksInMonth);
		timeOfConception = 0;
		mateSizeMod = 1f;
		sex = rand.nextInt(2);
		size_mod = (((rand.nextInt (degreeofdiversion+1)*(rand.nextBoolean()?1:-1)) / 10f) + 1F) * (1.0F - 0.1F * sex);

		//	We hijack the growingAge to hold the day of birth rather
		//	than number of ticks to next growth event. We want spawned
		//	animals to be adults, so we set their birthdays far enough back
		//	in time such that they reach adulthood now.
		//
		this.setAge((int) TFC_Time.getTotalDays() - getNumberOfDaysToAdult());
		if(!this.worldObj.isRemote)
		{
			//System.out.println("Wolf RM");
		}
	}
	public EntityWolfRM(World par1World, IAnimal mother, float F_size)
	{
		this(par1World);
		this.posX = ((EntityLivingBase)mother).posX;
		this.posY = ((EntityLivingBase)mother).posY;
		this.posZ = ((EntityLivingBase)mother).posZ;
		size_mod = (((rand.nextInt (1+1)*(rand.nextBoolean()?1:-1)) / 10f) + 1F) * (1.0F - 0.1F * sex) * (float)Math.sqrt((mother.getSize() + F_size)/1.9F);
		size_mod = Math.min(Math.max(size_mod, 0.7F),1.3f);

		//	We hijack the growingAge to hold the day of birth rather
		//	than number of ticks to next growth event.
		//
		this.setAge((int) TFC_Time.getTotalDays());
	}
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.30000001192092896D);

        if (this.isTamed())
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(TFC_MobData.WolfHealth/2);
        }
        else
        {
        	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(TFC_MobData.WolfHealth);
        }
    }
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if(this.isPregnant())
		{
			if(TFC_Time.getTotalTicks() >= timeOfConception + pregnancyRequiredTime)
			{
				int i = rand.nextInt(5) + 3;
				
				for (int x = 0; x<i;x++)
				{
					EntityWolfTFC baby = (EntityWolfTFC) createChildTFC(this);
					baby.setLocationAndAngles (posX+(rand.nextFloat()-0.5F)*2F,posY,posZ+(rand.nextFloat()-0.5F)*2F, 0.0F, 0.0F);
					baby.rotationYawHead = baby.rotationYaw;
					baby.renderYawOffset = baby.rotationYaw;
					worldObj.spawnEntityInWorld(baby);
					baby.setAge((int)TFC_Time.getTotalDays());
				}
				pregnant = false;
			}
		}
	}
	@Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int i = this.isTamed() ? TFC_MobData.WolfDamage : TFC_MobData.WolfDamage*2;
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)i);
    }
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	@Override
	public boolean isBreedingItem(ItemStack itemstack)
	{
		return !pregnant && RMItems.BreedingWolfFood.contains(itemstack.itemID);
	}
	@Override
    public void setTamed(boolean par1)
    {
        super.setTamed(par1);

        if (par1)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(TFC_MobData.WolfHealth/2);
        }
        else
        {
        	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(TFC_MobData.WolfHealth);
        }
    }
	@Override
	public EntityAgeable createChildTFC(EntityAgeable par1EntityAgeable)
	{
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
	@Override
    public EntityWolf spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        EntityWolf entitywolf = new EntityWolfRM(worldObj, this, par1EntityAgeable.getEntityData().getFloat("MateSize"));
        String s = this.getOwnerName();

        if (s != null && s.trim().length() > 0)
        {
            entitywolf.setOwner(s);
            entitywolf.setTamed(true);
        }
        return entitywolf;
    }
	@Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {	 		
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        if (this.isTamed())
        {
        	if(!this.worldObj.isRemote)
        	{
	    		par1EntityPlayer.addChatMessage(getGender()==GenderEnum.FEMALE?"Female":"Male");
	    		if(getGender()==GenderEnum.FEMALE && pregnant)
	    		{
	    			par1EntityPlayer.addChatMessage("Pregnant");
	    		}
        	}       	
        	if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isRemote && (itemstack == null || !this.isBreedingItem(itemstack)))
            {
        		this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.setPathToEntity((PathEntity)null);
                this.setTarget((Entity)null);
                this.setAttackTarget((EntityLivingBase)null);
            }
	        if (itemstack != null)
	        {
	            if (this.isBreedingItem(itemstack))
	            {
	            	if (!par1EntityPlayer.capabilities.isCreativeMode)
	                {
	            		--itemstack.stackSize;
	                }	
	                if (itemstack.stackSize <= 0)
	                {
	                    	par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
	                }                
	                if(this.getGrowingAge() == 0 && !getInLove())
	                {
	                	this.func_110196_bT();
	                }
	                this.heal(250);
	                return true;                   
	            }
	            else if (itemstack.itemID == Item.dyePowder.itemID)
	            {
	            	int i = BlockColored.getBlockFromDye(itemstack.getItemDamage());
	
	                if (i != this.getCollarColor())
	                {
	                	this.setCollarColor(i);
	
	                    if (!par1EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
	                    {
	                    	par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
	                    }	
	                    return true;
	                }
	            }
	        }
        }
	    if (itemstack != null && itemstack.itemID == Item.bone.itemID && !this.isAngry())
	    {
	    	if (!par1EntityPlayer.capabilities.isCreativeMode)
	        {
	    		--itemstack.stackSize;
		    }
		
		    if (itemstack.stackSize <= 0)
		    {
		    	par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
		    }
		
		    if (!this.worldObj.isRemote)
		    {
		    	if (this.rand.nextInt(5) == 0)
		        {
		    		this.setTamed(true);
		            this.setPathToEntity((PathEntity)null);
		            this.setAttackTarget((EntityLivingBase)null);
		            this.aiSit.setSitting(true);
		            this.setHealth(getMaxHealth());
		            this.setOwner(par1EntityPlayer.getCommandSenderName());
		            this.playTameEffect(true);
		            this.worldObj.setEntityState(this, (byte)7);
		         }
		         else
		         {
		        	 this.playTameEffect(false);
		             this.worldObj.setEntityState(this, (byte)6);
		         }
		    }		
		    return true;
    	}
	    else return false;
	}
	@Override
	public boolean isHungry()
	{
		return false;
	}
	@Override
	public boolean isStarving()
	{
		return false;
	}
}
