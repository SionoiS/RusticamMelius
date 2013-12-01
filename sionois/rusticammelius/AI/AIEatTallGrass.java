package sionois.rusticammelius.AI;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import sionois.rusticammelius.Mobs.IFarmAnimals;
import TFC.Core.TFC_Core;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


public class AIEatTallGrass extends EntityAIBase
{
	private EntityLiving theEntity;
	private World theWorld;
	private final double field_75404_b;
	
	/** A decrementing tick used for the sheep's head offset and animation. */
	int eatGrassTick;
	private int edibleBlockX;
	private int edibleBlockY;
	private int edibleBlockZ;
	
	public AIEatTallGrass(IFarmAnimals animal, double par1)
	{
		this.theEntity = (EntityLiving)animal;
		this.theWorld = theEntity.worldObj;
		this.field_75404_b = par1;
		this.setMutexBits(7);
	}
	@Override
    public boolean shouldExecute() 
    {
		IFarmAnimals animal = (IFarmAnimals)theEntity;
		if((animal.isHungry() | animal.isStarving()) && getNearbyEdibleBlockDistance())
        {
			return true;
        }
		else return false;
    }

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		System.out.println("startExecuting");
		this.eatGrassTick = 40;
		this.theWorld.setEntityState(this.theEntity, (byte)10);
		this.theEntity.getNavigator().tryMoveToXYZ((double)((float)this.edibleBlockX) + 0.5D, (double)(this.edibleBlockY), (double)((float)this.edibleBlockZ) + 0.5D, this.field_75404_b);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		System.out.println("resetTask");
		this.eatGrassTick = 0;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		if(this.eatGrassTick > 0)
		{
			if(this.theEntity instanceof EntityChickenRM && isEdibleGrassBlock(this.theEntity.worldObj, this.edibleBlockX, this.edibleBlockY, this.edibleBlockZ))
			{
				return true;
			}
			else if(isEdibleTallGrassBlock(this.theEntity.worldObj, this.edibleBlockX, this.edibleBlockY, this.edibleBlockZ))
			{
				return true;
			}
			else return false;
		}
		else return false;
	}

	public int getEatGrassTick()
	{
		return this.eatGrassTick;
	}

	/**
	 * Updates the task
	 */
	 @Override
     public void updateTask()
     {
		 
		 this.eatGrassTick = Math.max(0, this.eatGrassTick - 1);

		 if(this.theEntity.getDistanceSq((double)this.edibleBlockX, (double)(this.edibleBlockY), (double)this.edibleBlockZ) > 1.25D)
		 {
			 System.out.println("tryMoveToXYZ");
			 this.theEntity.getNavigator().tryMoveToXYZ((double)((float)this.edibleBlockX) + 0.5D, (double)(this.edibleBlockY), (double)((float)this.edibleBlockZ) + 0.5D, this.field_75404_b);
		 }
		 else
		 {
			 int grassID = this.theWorld.getBlockId(this.edibleBlockX, this.edibleBlockY - 1, this.edibleBlockZ);


			 if (TFC_Core.isLushGrass(grassID) && this.theEntity instanceof EntityChickenRM)
			 {
				 this.theWorld.playAuxSFX(2001, this.edibleBlockX, this.edibleBlockY - 1, this.edibleBlockZ, Block.grass.blockID);
				 TFC_Core.convertGrassToDirt(theWorld, this.edibleBlockX, this.edibleBlockY - 1, this.edibleBlockZ);
				 this.theEntity.eatGrassBonus();
			 }
			 else if (this.theWorld.getBlockId(this.edibleBlockX, this.edibleBlockY, this.edibleBlockZ) == Block.tallGrass.blockID)
			 {
				 this.theWorld.destroyBlock(this.edibleBlockX, this.edibleBlockY, this.edibleBlockZ, false);
				 this.theEntity.eatGrassBonus();
			 }
		 }
     }
	 protected boolean getNearbyEdibleBlockDistance()
	 {
		 int i = (int)this.theEntity.posY;
	     double d0 = 2.147483647E9D;

	     for (int j = (int)this.theEntity.posX - 1; (double)j < this.theEntity.posX + 1.0D; ++j)
	     {
	    	 for (int k = (int)this.theEntity.posZ - 1; (double)k < this.theEntity.posZ + 1.0D; ++k)
	         {
	    		 if(this.theEntity instanceof EntityChickenRM && this.isEdibleGrassBlock(this.theEntity.worldObj, j, i, k))
	    		 {
	    			 double d1 = this.theEntity.getDistanceSq((double)j, (double)i, (double)k);

	                 if (d1 < d0)
	                 {
	                	 this.edibleBlockX = j;
	                     this.edibleBlockY = i;
	                     this.edibleBlockZ = k;
	                     d0 = d1;
	                 }
	    		 }
	    		 else if (this.isEdibleTallGrassBlock(this.theEntity.worldObj, j, i, k))
	             {
	    			 double d1 = this.theEntity.getDistanceSq((double)j, (double)i, (double)k);

	                 if (d1 < d0)
	                 {
	                	 this.edibleBlockX = j;
	                     this.edibleBlockY = i;
	                     this.edibleBlockZ = k;
	                     d0 = d1;
	                 }
	             }
	         }
	     }
	     return d0 < 2.147483647E9D;
	 }
	 protected boolean isEdibleTallGrassBlock(World par1World, int par2, int par3, int par4)
	    {
	        int l = par1World.getBlockId(par2, par3, par4);

	        if (l == Block.tallGrass.blockID)
	        {
	        	return true;
	        }
	        else return false;
	    }
	    protected boolean isEdibleGrassBlock(World par1World, int par2, int par3, int par4)
	    {
	        int l = par1World.getBlockId(par2, par3, par4);

	        if (TFC_Core.isLushGrass(theWorld.getBlockId(par2, par3 - 1, par4)))
	        {
	        	return true;
	        }
	        else return false;
	    }
}
