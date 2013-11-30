package sionois.rusticammelius.Render;

import org.lwjgl.opengl.GL11;

import sionois.rusticammelius.Mobs.EntityPigRM;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import TFC.Entities.Mobs.EntityPigTFC;
import TFC.Render.RenderPigTFC;

public class RenderPigRM extends RenderPigTFC
{

	public RenderPigRM(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3)
	{
		super(par1ModelBase, par2ModelBase, par3);
	}
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
	{
		float scale = (((EntityPigRM)par1EntityLivingBase).size_mod/2)+0.5f;
		GL11.glScalef(scale, scale, scale);
	}
}
