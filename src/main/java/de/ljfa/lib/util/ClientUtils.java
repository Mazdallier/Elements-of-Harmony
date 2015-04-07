package de.ljfa.lib.util;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientUtils {
    
    public static final Minecraft minecraft = Minecraft.getMinecraft();
    
    public static int getRenderDistance() {
        int chunks = minecraft.gameSettings.renderDistanceChunks;
        if(chunks <= 0)
            chunks = 10;
        return 16 * chunks;
    }
    
    public static int getRenderDistanceSq() {
        int dist = getRenderDistance();
        return dist*dist;
    }
}