package sionois.rusticammelius.Render;

import org.lwjgl.opengl.GL11;

import sionois.rusticammelius.Mobs.EntityCowRM;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import TFC.Reference;
import TFC.API.Entities.IAnimal;
import TFC.API.Entities.IAnimal.GenderEnum;
import TFC.Core.TFC_Core;
import TFC.Entities.Mobs.EntityCowTFC;
import TFC.Render.RenderCowTFC;
import TFC.Render.Models.ModelCowTFC;

public class RenderCowRM extends RenderCowTFC
{
	private static final ResourceLocation CowTex = new ResourceLocation("textures/entity/cow/cow.png");
	private static final ResourceLocation BullTex = new ResourceLocation(Reference.ModID, "mob/bull.png");
	
	private ModelCowTFC modelcowtfc;
	
	public RenderCowRM(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);
		modelcowtfc = (ModelCowTFC) par1ModelBase;
		
	}
	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.shadowSize = 0.35f + (TFC_Core.getPercentGrown((IAnimal)par1Entity)*0.35f);
		super.doRender(par1Entity, par2, par4, par6, par8, par9);
	}

	protected ResourceLocation getTexture(EntityCowRM par1Entity)
	{
		if(par1Entity.getGender() == GenderEnum.MALE) {
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
