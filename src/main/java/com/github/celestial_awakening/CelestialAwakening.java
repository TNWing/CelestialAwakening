package com.github.celestial_awakening;

import com.github.celestial_awakening.events.AttachCapabilities;
import com.github.celestial_awakening.events.ClientEventsManager;
import com.github.celestial_awakening.events.EventManager;
import com.github.celestial_awakening.init.*;
import com.github.celestial_awakening.items.CustomTiers;
import com.github.celestial_awakening.networking.ModNetwork;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CelestialAwakening.MODID)
public class CelestialAwakening
{
    public static final String MODID = "celestial_awakening";
    private static final Logger LOGGER = LogUtils.getLogger();


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
/*
    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());


*/
    public CelestialAwakening()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        CREATIVE_MODE_TABS.register(modEventBus);
        registerTiers();
        ItemInit.ITEMS.register(modEventBus);
        LootInit.LOOT_SERIALIZER.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);
        LootInit.registerLootConditions();
        RecipeInit.RECIPE_SERIALIZER_DEFERRED_REGISTER.register(modEventBus);
        MobEffectInit.MOB_EFFECTS.register(modEventBus);



        MinecraftForge.EVENT_BUS.register(new EventManager());
        MinecraftForge.EVENT_BUS.register(new AttachCapabilities());
        MinecraftForge.EVENT_BUS.register(new ClientEventsManager());

        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        

    }
    public static ResourceLocation createResourceLocation(String str){
        return new ResourceLocation(MODID,str);
    }

    private void registerTiers(){
        TierSortingRegistry.registerTier(CustomTiers.MIDNIGHT_IRON, createResourceLocation("midnight_iron_tier"),
                Arrays.stream(new Object[]{Tiers.WOOD,Tiers.STONE}).toList(),Arrays.stream(new Object[]{Tiers.DIAMOND,Tiers.NETHERITE}).toList());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        ModNetwork.register();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){

        }
    }
}
