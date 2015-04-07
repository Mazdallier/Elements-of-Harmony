package de.ljfa.elofharmony.items;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import de.ljfa.elofharmony.CreativeTabEoh;
import de.ljfa.elofharmony.Reference;

public class ItemHelper {

    /** Sets the item's name and texture and registers it */
    public static <T extends Item> T register(T item, String name, String imageName) {
        item.setUnlocalizedName(Reference.MODID + ":" + name)
        .setTextureName(Reference.MODID + ":" + imageName)
        .setCreativeTab(CreativeTabEoh.EOH_TAB);
        GameRegistry.registerItem(item, name);
        return item;
    }

    /** Sets the item's name and texture and registers it */
    public static <T extends Item> T register(T item, String name) {
        return register(item, name, name);
    }

}