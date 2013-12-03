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
import TFC.Entities.Mobs.EntityChickenTFC;

public class EntityWildChicken extends EntityChickenTFC implements IWildAnimals
{
	int degreeOfDiversion = 2;
	
	public EntityWildChicken(World par1World)
	{
		super(par1World);
		this.tasks.taskEntries.clear();
		this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMateTFC(this,worldObj, 1.0f));
        this.tasks.addTask(2, new AITemptRM(this, 0.25D, true));
    	this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityPlayer.class, 50.0F, 1.5D, 2.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolfRM.class, 25f, 1.5D, 2.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityBear.class, 25f, 1.5D, 2.0D));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAILookIdle(this));

		this.timeUntilNextEgg = this.rand.nextInt(6000) + 24000;
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
			//System.out.println("Wild Chicken");
		}
	}
	public EntityWildChicken(World world, IAnimal mother, float f_size)
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
	public void eatGrassBonus(){}
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && RMItems.BreedingFood.contains(par1ItemStack.itemID);
    }
	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) 
	{
		return null;
	}
	@Override
	public EntityAgeable createChildTFC(EntityAgeable entityageable) {
		return new EntityChickenRM(worldObj, this, entityageable.getEntityData().getFloat("MateSize"));
	}
}
