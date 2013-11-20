package sionois.rusticammelius.AI;

import TFC.API.Entities.IAnimal;
import TFC.Core.TFC_Core;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


public class AIEatTallGrass extends EntityAIBase
{
	private EntityLiving theEntity;
	private World theWorld;
	
	/** A decrementing tick used for the sheep's head offset and animation. */
	int eatGrassTick;
	
	public AIEatTallGrass(IAnimal animal)
	{
		this.theEntity = (EntityLiving)animal;
		this.theWorld = theEntity.worldObj;
		this.setMutexBits(7);
	}
	@Override
	public boolean shouldExecute() 
	{
		IAnimal animal = (IAnimal)theEntity;
		if(animal.getHunger() < 144000 && theWorld.rand.nextInt(10) == 0)
		{
			int i = MathHelper.floor_double(this.theEntity.posX);
			int j = MathHelper.floor_double(this.theEntity.posY);
			int k = MathHelper.floor_double(this.theEntity.posZ);
			boolean isTallGrass = (this.theWorld.getBlockId(i, j, k) == Block.tallGrass.blockID && this.theWorld.getBlockMetadata(i, j, k) == 1);
			return isTallGrass;
		}
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.eatGrassTick = 40;
		this.theWorld.setEntityState(this.theEntity, (byte)10);
		this.theEntity.getNavigator().clearPathEntity();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.eatGrassTick = 0;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return this.eatGrassTick > 0;
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

		if (this.eatGrassTick == 1)
		{
			int i = MathHelper.floor_double(this.theEntity.posX);
			int j = MathHelper.floor_double(this.theEntity.posY);
			int k = MathHelper.floor_double(this.theEntity.posZ);

			int grassID = this.theWorld.getBlockId(i, j - 1, k);

			if (this.theWorld.getBlockId(i, j, k) == Block.tallGrass.blockID)
			{
				this.theWorld.destroyBlock(i, j, k, false);
				this.theEntity.eatGrassBonus();
			}
		}
	}
}
