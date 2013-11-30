package sionois.rusticammelius.Render;

import org.lwjgl.opengl.GL11;

import sionois.rusticammelius.Mobs.EntitySheepRM;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import TFC.Entities.Mobs.EntitySheepTFC;
import TFC.Render.RenderSheepTFC;

public class RenderSheepRM extends RenderSheepTFC
{
	private static final ResourceLocation SheepFurTexture = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	
	public RenderSheepRM(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3)
	{
		super(par1ModelBase, par2ModelBase, par3);
	}
	protected int setWoolColorAndRender(EntitySheepRM par1EntitySheep, int par2, float par3)
	{
		if (par2 == 0 && !par1EntitySheep.getSheared())
		{
			this.bindTexture(SheepFurTexture);
			float var4 = 1.0F;
			int var5 = par1EntitySheep.getFleeceColor();
			GL11.glColor3f(var4 * EntitySheepTFC.fleeceColorTable[var5][0], var4 * EntitySheepTFC.fleeceColorTable[var5][1], var4 * EntitySheepTFC.fleeceColorTable[var5][2]);
			return 1;
		}
		else
		{
			return -1;
		}
	}
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
	{
		float scale = (((EntitySheepRM)par1EntityLivingBase).size_mod/2)+0.5f;
		GL11.glScalef(scale, scale, scale);
	}
}
