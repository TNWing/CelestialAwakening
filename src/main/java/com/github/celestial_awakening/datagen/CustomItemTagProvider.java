package com.github.celestial_awakening.datagen;

import com.github.celestial_awakening.CelestialAwakening;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CustomItemTagProvider extends ItemTagsProvider {
    public CustomItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, CelestialAwakening.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        /*
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ItemInit.MOONSTONE_HELMET.get(),
                        ItemInit.MOONSTONE_CHESTPLATE.get(),
                        ItemInit.MOONSTONE_LEGGINGS.get(),
                        ItemInit.MOONSTONE_BOOTS .get())
                .add(ItemInit.SHADE_CAP.get(),ItemInit.SHADE_CLOAK.get(),ItemInit.SHADE_SLIPPERS.get(),ItemInit.SHADE_PANTS.get())
                .add(ItemInit.REMNANT_HELMET.get(),ItemInit.REMNANT_CHESTPLATE.get(),ItemInit.REMNANT_LEGGINGS.get(),ItemInit.REMNANT_BOOTS.get())
                .add(ItemInit.EVERLIGHT_CHESTPLATE.get(),ItemInit.EVERLIGHT_HELMET.get(),ItemInit.EVERLIGHT_BOOTS.get(),ItemInit.EVERLIGHT_LEGGINGS.get())
                .add(ItemInit.RADIANT_HELMET.get(),ItemInit.RADIANT_CHESTPLATE.get(),ItemInit.RADIANT_LEGGINGS.get(),ItemInit.RADIANT_BOOTS.get())
                .add(ItemInit.UMBRA_HELMET.get(),ItemInit.UMBRA_BOOTS.get(),ItemInit.UMBRA_CHESTPLATE.get(),ItemInit.UMBRA_LEGGINGS.get())


        ;

         */
    }
}
