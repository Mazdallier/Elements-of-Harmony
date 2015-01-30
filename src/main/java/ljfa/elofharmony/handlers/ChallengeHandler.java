package ljfa.elofharmony.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChallengeHandler {
    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event) {
        World world = event.entityLiving.worldObj;
        if(world.isRemote || !(event.entityLiving instanceof EntityPlayer))
            return;
        
        EntityPlayer player = (EntityPlayer)event.entityLiving;
        if(player.getEntityData().hasKey("eoh:challenge")) {
            NBTTagCompound tag = player.getEntityData().getCompoundTag("eoh:challenge");
            
        }
    }
    
    public static boolean isChallengeRunning(EntityPlayer player) {
        return player.getEntityData().hasKey("eoh:challenge");
    }
}
