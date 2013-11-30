package sionois.rusticammelius;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import sionois.rusticammelius.Mobs.EntityCowRM;
import sionois.rusticammelius.Mobs.EntityPigRM;
import sionois.rusticammelius.Mobs.EntitySheepRM;
import sionois.rusticammelius.Render.RenderChickenRM;
import sionois.rusticammelius.Render.RenderCowRM;
import sionois.rusticammelius.Render.RenderPigRM;
import sionois.rusticammelius.Render.RenderSheepRM;
import TFC.Render.Models.ModelChickenTFC;
import TFC.Render.Models.ModelCowTFC;
import TFC.Render.Models.ModelPigTFC;
import TFC.Render.Models.ModelSheep1TFC;
import TFC.Render.Models.ModelSheep2TFC;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public void registerRenderInformation()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCowRM.class, new RenderCowRM(new ModelCowTFC(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySheepRM.class, new RenderSheepRM(new ModelSheep2TFC(),new ModelSheep1TFC(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(EntityChickenRM.class, new RenderChickenRM(new ModelChickenTFC(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPigRM.class, new RenderPigRM(new ModelPigTFC(), new ModelPigTFC(0.5F), 0.7F));
	}

}
