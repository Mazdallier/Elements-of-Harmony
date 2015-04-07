package de.ljfa.elofharmony.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.ljfa.elofharmony.tile.TileLocker;

public class BlockLocker extends BlockBase implements ITileEntityProvider {

    public BlockLocker() {
        super("locker", Material.wood);
        setHardness(2.0f);
        setResistance(20.0f);
        setStepSound(soundTypeWood);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileLocker();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if(!world.isRemote)
            BlockHelper.spillInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }
}