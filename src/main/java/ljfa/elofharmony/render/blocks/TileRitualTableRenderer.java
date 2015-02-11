package ljfa.elofharmony.render.blocks;

import ljfa.elofharmony.blocks.ModBlocks;
import ljfa.elofharmony.tile.TileRitualTable;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

public class TileRitualTableRenderer extends TileEntitySpecialRenderer {
    private Tessellator tess;
    private RenderItem renderItem;
    private double minU, maxU, minV, maxV;
    
    public TileRitualTableRenderer() {
        tess = Tessellator.instance;
        renderItem = new RenderItem() {
            @Override
            public boolean shouldBob() {
                return false;
            }
        };
        renderItem.setRenderManager(RenderManager.instance);
        
        IIcon icon = ModBlocks.planks_flutter.getIcon(0, 0);
        minU = icon.getMinU();
        maxU = icon.getMaxU();
        minV = icon.getMinV();
        maxV = icon.getMaxV();
    }
    
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float par5) {
        if(!(te instanceof TileRitualTable))
            return;
        TileRitualTable tile = (TileRitualTable)te;
        
        bindTexture(TextureMap.locationBlocksTexture);
        
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        tess.startDrawingQuads();
        GL11.glDisable(GL11.GL_LIGHTING);
        
        //Y-
        tess.addVertexWithUV(0, 0, 0, minU, minV);
        tess.addVertexWithUV(1, 0, 0, maxU, minV);
        tess.addVertexWithUV(1, 0, 1, maxU, maxV);
        tess.addVertexWithUV(0, 0, 1, minU, maxV);

        //Y+
        tess.addVertexWithUV(0, 0.5, 0, minU, minV);
        tess.addVertexWithUV(0, 0.5, 1, minU, maxV);
        tess.addVertexWithUV(1, 0.5, 1, maxU, maxV);
        tess.addVertexWithUV(1, 0.5, 0, maxU, minV);
        
        //Z-
        tess.addVertexWithUV(0, 0,   0, minU, minV);
        tess.addVertexWithUV(0, 0.5, 0, minU, maxV);
        tess.addVertexWithUV(1, 0.5, 0, maxU, maxV);
        tess.addVertexWithUV(1, 0,   0, maxU, minV);
        
        //Z+
        tess.addVertexWithUV(0, 0,   1, minU, minV);
        tess.addVertexWithUV(1, 0,   1, maxU, minV);
        tess.addVertexWithUV(1, 0.5, 1, maxU, maxV);
        tess.addVertexWithUV(0, 0.5, 1, minU, maxV);
        
        //X-
        tess.addVertexWithUV(0, 0,   0, minU, minV);
        tess.addVertexWithUV(0, 0,   1, maxU, minV);
        tess.addVertexWithUV(0, 0.5, 1, maxU, maxV);
        tess.addVertexWithUV(0, 0.5, 0, minU, maxV);
        
        //X+
        tess.addVertexWithUV(1, 0,   0, minU, minV);
        tess.addVertexWithUV(1, 0.5, 0, minU, maxV);
        tess.addVertexWithUV(1, 0.5, 1, maxU, maxV);
        tess.addVertexWithUV(1, 0,   1, maxU, minV);
        
        tess.draw();
        
        
        if(tile.getStackInSlot(0) != null) {
            GL11.glTranslated(0.5, 0.65, 0.5);
            EntityItem shownItem = new EntityItem(tile.getWorldObj());
            shownItem.setEntityItemStack(tile.getStackInSlot(0));
            shownItem.hoverStart = 0.0f;
            renderItem.doRender(shownItem, 0, 0, 0, 0, 0);
        }
        
        GL11.glPopMatrix();
    }

}