package ljfa.elofharmony.worldgen;

import java.util.Arrays;
import java.util.Random;

import ljfa.elofharmony.Config;
import ljfa.elofharmony.blocks.ModBlocks;
import ljfa.elofharmony.util.LjfaMathHelper;
import ljfa.elofharmony.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DecorationPoisonJoke {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {
        if(!(event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) || event.type != EventType.FLOWERS)
            return;
        Random rand = event.rand;

        int xGen = event.chunkX + rand.nextInt(16) + 8;
        int zGen = event.chunkZ + rand.nextInt(16) + 8;
        
        if(!canGeneratePoisonJoke(event.world.getBiomeGenForCoords(xGen, zGen)) || rand.nextInt(256) >= Config.pjSpawnChance)
            return;
        LogHelper.debug("Attempting to generate Poison Joke at (%d, %d)", xGen, zGen);
        
        double xSize = LjfaMathHelper.triangularDouble(rand, 2.0, 6.0);
        double zSize = LjfaMathHelper.triangularDouble(rand, 2.0, 6.0);
        
        int yGen = event.world.getTopSolidOrLiquidBlock(xGen, zGen);
        
        // Bounding box of the patch
        int minX = (int)Math.floor(xGen - xSize);
        int maxX = (int)Math.ceil(xGen + xSize);
        int minY = yGen - 2;
        int maxY = yGen + 2;
        int minZ = (int)Math.floor(zGen - zSize);
        int maxZ = (int)Math.ceil(zGen + zSize);
        
        for(int x = minX; x <= maxX; x++)
            for(int z = minZ; z <= maxZ; z++) {
                // The patch is roughly diamond-shaped (1-sphere)
                if(Math.abs(x - xGen)/xSize + Math.abs(z - zGen)/zSize <= 0.5 + rand.nextDouble()) {
                    for(int y = minY; y <= maxY; y++) {
                        if(event.world.isAirBlock(x, y, z) && ModBlocks.poisonjoke.canBlockStay(event.world, x, y, z)) {
                            event.world.setBlock(x, y, z, ModBlocks.poisonjoke, 7, 2);
                            break;
                        }
                    }
                }
            }
    }
    
    public boolean canGeneratePoisonJoke(BiomeGenBase biome) {
        // Generate in forests that aren't cold or sparse, as well as in swamps
        // and spooky biomes
        return (BiomeDictionary.isBiomeOfType(biome, Type.FOREST)
                && !BiomeDictionary.isBiomeOfType(biome, Type.CONIFEROUS)
                && !BiomeDictionary.isBiomeOfType(biome, Type.SPARSE))
            || BiomeDictionary.isBiomeOfType(biome, Type.SWAMP)
            || BiomeDictionary.isBiomeOfType(biome, Type.SPOOKY);
    }
}
