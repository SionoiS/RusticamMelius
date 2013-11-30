package sionois.rusticammelius;

import java.io.File;

import sionois.rusticammelius.Handler.MobRemplacerHandler;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import TFC.TerraFirmaCraft;
import TFC.Core.Util.Localization;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid=ModRef.ModID, name=ModRef.ModName, version=ModRef.ModVersion ,  dependencies = "after:Terrafirmacraft")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class RusticamMelius
{
	 @Instance("RusticamMelius")
     public static RusticamMelius instance;
     
     @SidedProxy(clientSide=ModRef.CLIENT_PROXY_CLASS, serverSide=ModRef.SERVER_PROXY_CLASS)
     public static CommonProxy proxy;
     
     @EventHandler     
     public void preInit(FMLPreInitializationEvent event) 
     {               
             loadConfig();
             
             proxy.registerTileEntities();

     }
     
     @EventHandler
     public void load(FMLInitializationEvent event) 
     {
    	 Localization.addLocalization("/assets/rusticammelius/lang/", "en_US");
     
         Recipes.addRecipes();
         
         proxy.registerRenderInformation();                                           
     }
     
     @EventHandler
     public void postInit(FMLPostInitializationEvent event) 
     {
             MinecraftForge.EVENT_BUS.register(new MobRemplacerHandler());
     }
     public void loadConfig() 
     {
    	 Configuration config;
         try
         {
        	 config = new Configuration (new File(TerraFirmaCraft.proxy.getMinecraftDir(), "/config/RusticamMelius.cfg"));
        	 config.load();
         }
         catch (Exception e)
         {
        	 System.out.println(new StringBuilder().append("[RusticamMelius] Error while trying to access configuration !").toString());
        	 config = null;
         }
         System.out.println(new StringBuilder().append("[RusticamMelius] Loading Config").toString());
                 
         /**Blocks*/

         RMBlocks.LoadBlocks();
         RMBlocks.RegisterBlocks();
                 
         /**Items*/ 
                 
         RMItems.LoadItems();
         RMItems.RegisterItems();
         
         if (config != null)
         {
                         config.save();
         }
     }
}
