package de.ljfa.elofharmony.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import de.ljfa.elofharmony.challenges.Challenge;
import de.ljfa.elofharmony.challenges.ChallengeHolder;

public class ChallengeHandler {

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        World world = event.player.worldObj;
        if(world.isRemote || event.phase != Phase.START)
            return;
        
        EntityPlayerMP player = (EntityPlayerMP)event.player;
        Challenge ch = getChallenge(player);
        if(ch != null) {
            ch.onTick();
            if(!ch.checkRestriction()) {
                ChallengeHandler.abortChallenge(ch);
            }
            if(world.getWorldTime() % 16 == 0) {
                if(ch.checkCondition()) {
                    ChallengeHandler.endChallenge(ch);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
        Challenge ch = getChallenge(player);
        if(ch != null && !ch.mayPickUp(event.item.getEntityItem())) {
            event.item.delayBeforeCanPickup = 10;
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if(event.entity instanceof EntityPlayerMP) {
            Challenge ch = getChallenge((EntityPlayerMP)event.entity);
            if(ch != null)
                ch.onPlayerHurt(event.source, event.ammount);
        }
    }
    
    @SubscribeEvent
    public void onEntityConstruct(EntityConstructing event) {
        if(event.entity instanceof EntityPlayerMP)
            ChallengeHolder.initPlayer((EntityPlayerMP)event.entity);
    }
    
    public static boolean tryStartChallenge(Challenge ch) {
        if(ch.checkStartingCondition()) {
            startChallenge(ch);
            return true;
        }
        else
            return false;
    }
    
    public static boolean tryAbortChallenge(EntityPlayer player) {
        Challenge ch = getChallenge((EntityPlayerMP)player);
        if(ch != null) {
            abortChallenge(ch);
            return true;
        }
        else
            return false;
    }
    
    public static Challenge getChallenge(EntityPlayerMP player) {
        return ChallengeHolder.get(player).getChallenge();
    }
    
    public static boolean hasChallenge(EntityPlayerMP player) {
        return getChallenge(player) != null;
    }
    
    private static void startChallenge(Challenge ch) {
        ChallengeHolder.get(ch.getPlayer()).setChallenge(ch);
        ch.onStart();
    }
    
    private static void abortChallenge(Challenge ch) {
        ch.onAbort();
        ChallengeHolder.get(ch.getPlayer()).clearChallenge();
    }
    
    private static void endChallenge(Challenge ch) {
        ch.onComplete();
        ChallengeHolder.get(ch.getPlayer()).clearChallenge();
    }
}
