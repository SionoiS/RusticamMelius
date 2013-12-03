package sionois.rusticammelius;

import sionois.rusticammelius.Mobs.EntityChickenRM;
import sionois.rusticammelius.Mobs.EntityCowRM;
import sionois.rusticammelius.Mobs.EntityPigRM;
import sionois.rusticammelius.Mobs.EntitySheepRM;
import sionois.rusticammelius.Mobs.EntityWildChicken;
import sionois.rusticammelius.Mobs.EntityWildCow;
import sionois.rusticammelius.Mobs.EntityWildPig;
import sionois.rusticammelius.Mobs.EntityWildSheep;
import sionois.rusticammelius.Mobs.EntityWolfRM;
import TFC.Render.RenderChickenTFC;
import TFC.Render.RenderCowTFC;
import TFC.Render.RenderPigTFC;
import TFC.Render.RenderSheepTFC;
import TFC.Render.RenderWolfTFC;
import TFC.Render.Models.ModelChickenTFC;
import TFC.Render.Models.ModelCowTFC;
import TFC.Render.Models.ModelPigTFC;
import TFC.Render.Models.ModelSheep1TFC;
import TFC.Render.Models.ModelSheep2TFC;
import TFC.Render.Models.ModelWolfTFC;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public void registerRenderInformation()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCowRM.class, new RenderCowTFC(new ModelCowTFC(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySheepRM.class, new RenderSheepTFC(new ModelSheep2TFC(),new ModelSheep1TFC(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(EntityChickenRM.class, new RenderChickenTFC(new ModelChickenTFC(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPigRM.class, new RenderPigTFC(new ModelPigTFC(), new ModelPigTFC(0.5F), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntityWolfRM.class, new RenderWolfTFC(new ModelWolfTFC(),new ModelWolfTFC(), 0.5F));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityWildCow.class, new RenderCowTFC(new ModelCowTFC(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntityWildSheep.class, new RenderSheepTFC(new ModelSheep2TFC(),new ModelSheep1TFC(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(EntityWildChicken.class, new RenderChickenTFC(new ModelChickenTFC(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityWildPig.class, new RenderPigTFC(new ModelPigTFC(), new ModelPigTFC(0.5F), 0.7F));
	}

}
