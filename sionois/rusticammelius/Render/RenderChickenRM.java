package sionois.rusticammelius.Render;

import org.lwjgl.opengl.GL11;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.Render.RenderChickenTFC;

public class RenderChickenRM extends RenderChickenTFC
{

	public RenderChickenRM(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);
	}
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
	{
		float scale = (((EntityChickenRM)par1EntityLivingBase).size_mod/3)+0.5f;
		GL11.glScalef(scale, scale, scale);
	}
}
