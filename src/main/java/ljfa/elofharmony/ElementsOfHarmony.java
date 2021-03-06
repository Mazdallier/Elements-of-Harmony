package ljfa.elofharmony;

import ljfa.elofharmony.blocks.ModBlocks;
import ljfa.elofharmony.challenges.ChallengeRegistry;
import ljfa.elofharmony.handlers.ChallengeHandler;
import ljfa.elofharmony.handlers.PoisonJokeHandler;
import ljfa.elofharmony.items.ModItems;
import ljfa.elofharmony.proxy.CommonProxy;
import ljfa.elofharmony.worldgen.DecorationPoisonJoke;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION,
guiFactory = Reference.GUI_FACTORY_CLASS)
public class ElementsOfHarmony {
    @Mod.Instance(Reference.MODID)
    public static ElementsOfHarmony instance;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
        ModBlocks.init();
        ModItems.init();
        proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if(Config.pjNumEffects != 0)
            MinecraftForge.EVENT_BUS.register(new PoisonJokeHandler());
        if(Config.pjSpawnChance != 0)
            MinecraftForge.TERRAIN_GEN_BUS.register(new DecorationPoisonJoke());
        FMLCommonHandler.instance().bus().register(new ChallengeHandler());
        
        ModRecipes.addOredict();
        ModRecipes.addRecipes();
        ChallengeRegistry.initChallenges();
        proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
