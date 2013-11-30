package sionois.rusticammelius.Render;

import org.lwjgl.opengl.GL11;

import sionois.rusticammelius.Mobs.EntityCowRM;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import TFC.Reference;
import TFC.API.Entities.IAnimal.GenderEnum;
import TFC.Entities.Mobs.EntityCowTFC;
import TFC.Render.RenderCowTFC;

public class RenderCowRM extends RenderCowTFC
{
	private static final ResourceLocation CowTex = new ResourceLocation("textures/entity/cow/cow.png");
	private static final ResourceLocation BullTex = new ResourceLocation(Reference.ModID, "mob/bull.png");
	
	public RenderCowRM(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);
	}
	protected ResourceLocation getTexture(EntityCowRM entity)
	{
		if(entity.getGender() == GenderEnum.MALE) {
			return BullTex;
		} else {
			return CowTex;
		}
	}
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
	{
		float scale = ((EntityCowRM)par1EntityLivingBase).size_mod;
		GL11.glScalef(scale, scale, scale);
	}
	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.getTexture((EntityCowRM)par1Entity);
	}
}
