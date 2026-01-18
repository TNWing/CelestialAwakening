package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.projectile.ArrowType;
import com.github.celestial_awakening.items.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, CelestialAwakening.MODID);

    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUNAR_SCALE = ITEMS.register("lunar_scale", () ->new CustomItem(new Item.Properties(),"tooltip.celestial_awakening.lunar_scale"));
    public static final RegistryObject<Item> LUNAR_ARROW = ITEMS.register("lunar_arrow", () ->new CustomArrowItem(new Item.Properties(),ArrowType.LUNAR));

    public static final RegistryObject<Item> MOON_SCYTHE=ITEMS.register("moon_scythe",()->new MoonScythe(new Item.Properties().stacksTo(1).durability(640)));
    public static final RegistryObject<Item> MOONLIGHT_REAPER=ITEMS.register("moonlight_reaper",()->new MoonlightReaper(new Item.Properties().stacksTo(1).durability(2560)));
    public static final RegistryObject<Item> MOONSTONE_HELMET = ITEMS.register("moonstone_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.LUNAR, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MOONSTONE_CHESTPLATE = ITEMS.register("moonstone_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.LUNAR, ArmorItem.Type.CHESTPLATE, new Item.Properties(),1));
    public static final RegistryObject<Item> MOONSTONE_LEGGINGS = ITEMS.register("moonstone_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.LUNAR, ArmorItem.Type.LEGGINGS, new Item.Properties(),1));
    public static final RegistryObject<Item> MOONSTONE_BOOTS = ITEMS.register("moonstone_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.LUNAR, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LUNAR_DIAL = ITEMS.register("lunar_dial", ()->new LunarDial(new Item.Properties().defaultDurability(1000)));

    public static final RegistryObject<Item> ONYX_FUR = ITEMS.register("onyx_fur", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_HELMET = ITEMS.register("umbra_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.UMBRA, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_CHESTPLATE = ITEMS.register("umbra_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.UMBRA, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_LEGGINGS = ITEMS.register("umbra_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.UMBRA, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_BOOTS = ITEMS.register("umbra_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.UMBRA, ArmorItem.Type.BOOTS, new Item.Properties()));



    public static final RegistryObject<Item> SUNSTONE = ITEMS.register("sunstone", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIFE_FRAG = ITEMS.register("life_fragment", ()->new CustomItem(new Item.Properties(),"tooltip.celestial_awakening.life_frag"));
    public static final RegistryObject<Item> FLUORESCENT_BOW=ITEMS.register("fluorescent_bow",()->new FluorescentBow(new Item.Properties().stacksTo(1).durability(448)));
    public static final RegistryObject<Item> STAR_GAZER=ITEMS.register("star_gazer",()->new Stargazer(new Item.Properties().stacksTo(1).durability(1024)));
    public static final RegistryObject<Item> SOLAR_ARROW = ITEMS.register("solar_arrow", () ->new CustomArrowItem(new Item.Properties(), ArrowType.SOLAR));
    public static final RegistryObject<Item> RADIANT_HELMET = ITEMS.register("radiant_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.RADIANT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_CHESTPLATE = ITEMS.register("radiant_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.RADIANT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_LEGGINGS = ITEMS.register("radiant_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.RADIANT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_BOOTS = ITEMS.register("radiant_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.RADIANT, ArmorItem.Type.BOOTS, new Item.Properties()));

    //public static final RegistryObject<Item> COSMIC_PEARL=ITEMS.register("cosmic_pearl",()->new CosmicPearl(new Item.Properties().stacksTo(16),"tooltip_celestial_awakening_cosmic_pearl"));

    public static final RegistryObject<Item> SCORCHED_STONE=ITEMS.register ("scorched_stone",()->new BlockItem(BlockInit.SCORCHED_STONE.get(),new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> VOLCANIC_CLAYMORE=ITEMS.register("volcanic_claymore",()->new CA_UntieredSword(new Item.Properties().fireResistant().stacksTo(1).durability(300),10.5f,-3.2f));
    public static final RegistryObject<Item> SCORCHED_HELMET = ITEMS.register("scorched_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.SCORCHED, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SCORCHED_CHESTPLATE = ITEMS.register("scorched_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.SCORCHED, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SCORCHED_LEGGINGS = ITEMS.register("scorched_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.SCORCHED, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SCORCHED_BOOTS = ITEMS.register("scorched_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.SCORCHED, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SEARED_STONE_PICKAXE=ITEMS.register("seared_stone_pickaxe",()->new SearedStonePickaxe(CustomTiers.SEARED_STONE,1, -2.8F,new Item.Properties()));
    public static final RegistryObject<Item> SEARED_STONE_AXE=ITEMS.register("seared_stone_axe",()->new SearedStoneAxe(CustomTiers.SEARED_STONE,6.0F, -3.1F,new Item.Properties()));


    public static final RegistryObject<Item> GAIA_PLATE=ITEMS.register("gaia_plate",() ->new Item(new Item.Properties()));
    //public static final RegistryObject<Item> TERRA_GLAIVE=ITEMS.register("terra_glaive",()->new TerraGlaive(new Item.Properties().stacksTo(1).durability(800),8.5f,-2.9f));
    public static final RegistryObject<Item> PLATE_HELMET=ITEMS.register("plate_helmet",()->new CustomArmorItem(
            CustomArmorMaterial.PLATE, ArmorItem.Type.HELMET,new Item.Properties()
    ));
    public static final RegistryObject<Item> PLATE_CHESTPLATE=ITEMS.register("plate_chestplate",()->new CustomArmorItem(
            CustomArmorMaterial.PLATE, ArmorItem.Type.CHESTPLATE,new Item.Properties()
    ));
    public static final RegistryObject<Item> PLATE_LEGGINGS=ITEMS.register("plate_leggings",()->new CustomArmorItem(
            CustomArmorMaterial.PLATE, ArmorItem.Type.LEGGINGS,new Item.Properties()
    ));
    public static final RegistryObject<Item> PLATE_BOOTS=ITEMS.register("plate_boots",()->new CustomArmorItem(
            CustomArmorMaterial.PLATE, ArmorItem.Type.BOOTS,new Item.Properties()
    ));
    public static final RegistryObject<Item> SHIMMER_CUBE = ITEMS.register("shimmer_cube", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> SUN_STAFF=ITEMS.register("sun_staff",()->new SunStaff(new Item.Properties().stacksTo(1).durability(640)));

    public static final RegistryObject<Item> DYING_LIGHT_ESSENCE =
            ITEMS.register("dying_light_essence", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_HELMET = ITEMS.register("remnant_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.REMNANT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_CHESTPLATE = ITEMS.register("remnant_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.REMNANT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_LEGGINGS = ITEMS.register("remnant_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.REMNANT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_BOOTS = ITEMS.register("remnant_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.REMNANT, ArmorItem.Type.BOOTS, new Item.Properties()));



    public static final RegistryObject<Item> LUNA_TOME=ITEMS.register("luna_tome",()->new LunaTomeItem(new Item.Properties(),"tooltip.celestial_awakening.luna_tome",0xFFFFFF));

    //maybe make them slightly worse than iron but have the buffs to offset the weaker stats?
    public static final RegistryObject<Item> MIDNIGHT_IRON_INGOT = ITEMS.register("midnight_iron_ingot", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_SWORD=ITEMS.register("midnight_iron_sword",()->new MidnightIronSword(CustomTiers.MIDNIGHT_IRON,3,-2.4f,new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_PICKAXE=ITEMS.register("midnight_iron_pickaxe",()->new MidnightIronPickaxe(CustomTiers.MIDNIGHT_IRON,1, -2.8F,new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_AXE=ITEMS.register("midnight_iron_axe",()->new MidnightIronAxe(CustomTiers.MIDNIGHT_IRON,6.0F, -3.1F,new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_SHOVEL=ITEMS.register("midnight_iron_shovel",()->new MidnightIronShovel(CustomTiers.MIDNIGHT_IRON,1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_HOE=ITEMS.register("midnight_iron_hoe",()->new MidnightIronHoe(CustomTiers.MIDNIGHT_IRON,-2, -1.0F,new Item.Properties()));

    public static final RegistryObject<Item> LUNULA_ROCK = ITEMS.register("lunula_rock", () ->new CustomItem(new Item.Properties(),"tooltip.celestial_awakening.lunula_rock"));
    public static final RegistryObject<Item> KNIGHTMARE_HELMET = ITEMS.register("knightmare_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.KNIGHTMARE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_CHESTPLATE = ITEMS.register("knightmare_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.KNIGHTMARE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_LEGGINGS = ITEMS.register("knightmare_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.KNIGHTMARE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_BOOTS = ITEMS.register("knightmare_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.KNIGHTMARE, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> EVERLIGHT_HELMET=ITEMS.register("everlight_helmet",
            ()->new CustomArmorItem(CustomArmorMaterial.EVERLIGHT,ArmorItem.Type.HELMET,new Item.Properties(),1.5f));
    public static final RegistryObject<Item> EVERLIGHT_CHESTPLATE=ITEMS.register("everlight_chestplate",
            ()->new CustomArmorItem(CustomArmorMaterial.EVERLIGHT,ArmorItem.Type.CHESTPLATE,new Item.Properties(),0));
    public static final RegistryObject<Item> EVERLIGHT_LEGGINGS=ITEMS.register("everlight_leggings",
            ()->new CustomArmorItem(CustomArmorMaterial.EVERLIGHT,ArmorItem.Type.LEGGINGS,new Item.Properties(),0));
    public static final RegistryObject<Item> EVERLIGHT_BOOTS=ITEMS.register("everlight_boots",
            ()->new CustomArmorItem(CustomArmorMaterial.EVERLIGHT,ArmorItem.Type.BOOTS,new Item.Properties(),1.5f));

    public static final RegistryObject<Item> COSMIC_HIDE =
            ITEMS.register("cosmic_hide", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> PULSATING_DARKNESS =
            ITEMS.register("pulsating_darkness", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHADE_CAP=
            ITEMS.register("shade_helmet",()->new CustomArmorItem(CustomArmorMaterial.SHADE,ArmorItem.Type.HELMET,new Item.Properties(),1));
    public static final RegistryObject<Item> SHADE_CLOAK=
            ITEMS.register("shade_chestplate",()->new CustomArmorItem(CustomArmorMaterial.SHADE,ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> SHADE_PANTS=
            ITEMS.register("shade_leggings",()->new CustomArmorItem(CustomArmorMaterial.SHADE,ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> SHADE_SLIPPERS=
            ITEMS.register("shade_boots",()->new CustomArmorItem(CustomArmorMaterial.SHADE,ArmorItem.Type.BOOTS,new Item.Properties(),1));


    //public static final RegistryObject<Item> PROTO_STAR =ITEMS.register("proto_star",()->new BlockItem(BlockInit.PROTO_STAR.get(), new Item.Properties().stacksTo(1)));

}
