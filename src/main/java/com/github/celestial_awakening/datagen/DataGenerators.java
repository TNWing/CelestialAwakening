package com.github.celestial_awakening.datagen;

import com.github.celestial_awakening.CelestialAwakening;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CelestialAwakening.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator=event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


        //generator.addProvider(event.includeClient(),new CustomItemModelProvider(packOutput,existingFileHelper));

        CustomBlockTagProvider blockTagGenerator = generator.addProvider(event.includeServer(),
                new CustomBlockTagProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(),new CustomItemTagProvider(packOutput,lookupProvider, blockTagGenerator.contentsGetter(),existingFileHelper));

        generator.addProvider(event.includeServer(),new LootProvider(packOutput));
    }
}
