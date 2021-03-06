package ljfa.elofharmony.handlers;

import ljfa.elofharmony.challenges.Challenge;
import ljfa.elofharmony.challenges.ChallengeRegistry;
import ljfa.elofharmony.tile.TileRitualTable;
import ljfa.elofharmony.util.ChatHelper;
import ljfa.elofharmony.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ChallengeHandler {
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        World world = event.player.worldObj;
        if(event.phase != Phase.END || world.isRemote)
            return;

        NBTTagCompound playerData = event.player.getEntityData();
        if(playerData.hasKey("eoh:challenge")) {
            NBTTagCompound tag = playerData.getCompoundTag("eoh:challenge");
            int chID = tag.getInteger("id");
            Challenge ch = ChallengeRegistry.fromId(chID);
            
            ch.onTick(event.player, tag);
            if(!ch.checkRestriction(event.player, tag)) {
                abortChallenge(ch, event.player, tag);
                ChatHelper.toPlayer(event.player, "You failed the challenge!");
            }
            if((world.getWorldTime() & 31) == 0) {
                ChatHelper.toPlayer(event.player, "The challenge is running");
                if(ch.checkCondition(event.player, tag)) {
                    endChallenge(ch, event.player, tag);
                    ChatHelper.toPlayer(event.player, "You completed the challenge!");
                }
            }
        }
    }
    
    public static boolean startChallenge(EntityPlayer player, Challenge ch, TileRitualTable tile) {
        if(ch.checkStartingCondition(player, tile)) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("id", ch.id);
            tag.setInteger("tableX", tile.xCoord);
            tag.setInteger("tableY", tile.yCoord);
            tag.setInteger("tableZ", tile.zCoord);
            tag.setInteger("tableDim", tile.getWorldObj().provider.dimensionId);
            player.getEntityData().setTag("eoh:challenge", tag);
            ch.onStart(player, tag, tile);
            ChatHelper.toPlayer(player, "The challenge is on");
            return true;
        } else {
            ChatHelper.toPlayer(player, "You're not quite ready for the challenge");
            return false;
        }
    }
    
    private static void endChallenge(Challenge ch, EntityPlayer player, NBTTagCompound data) {
        ch.onComplete(player, data);
        int x = data.getInteger("tableX"), y = data.getInteger("tableY"), z = data.getInteger("tableZ");
        int dim = data.getInteger("tableDim");
        TileEntity te = DimensionManager.getWorld(dim).getTileEntity(x, y, z);
        if(te instanceof TileRitualTable) {
            TileRitualTable tile = (TileRitualTable)te;
            tile.endChallenge();
            tile.setInventorySlotContents(0, ch.getResult());
        } else {
            LogHelper.error("Wrong or missing tile entity at dim %d (%d,%d,%d)", dim, x, y, z);
        }
        player.getEntityData().removeTag("eoh:challenge");
    }
    
    public static boolean hasChallengeRunning(EntityPlayer player) {
        return player.getEntityData().hasKey("eoh:challenge");
    }
    
    public static Challenge getCurrentChallenge(EntityPlayer player, NBTTagCompound data) {
        if(hasChallengeRunning(player))
            return ChallengeRegistry.fromId(data.getInteger("id"));
        else
            return null;
    }
    
    public static void abortChallenge(Challenge ch, EntityPlayer player, NBTTagCompound data) {
        if(hasChallengeRunning(player)) {
            ch.onAbort(player, data);
            int x = data.getInteger("tableX"), y = data.getInteger("tableY"), z = data.getInteger("tableZ");
            int dim = data.getInteger("tableDim");
            TileEntity tile = DimensionManager.getWorld(dim).getTileEntity(x, y, z);
            if(tile instanceof TileRitualTable) {
                ((TileRitualTable)tile).endChallenge();
            } else {
                LogHelper.error("Wrong or missing tile entity at dim %d (%d,%d,%d)", dim, x, y, z);
            }
            player.getEntityData().removeTag("eoh:challenge");
        }
    }
    
    public static NBTTagCompound getChallengeData(EntityPlayer player) {
        if(hasChallengeRunning(player))
            return player.getEntityData().getCompoundTag("eoh:challenge");
        else
            return null;
    }
}
