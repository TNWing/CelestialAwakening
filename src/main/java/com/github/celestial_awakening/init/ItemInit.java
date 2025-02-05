package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.items.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, CelestialAwakening.MODID);

    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOON_SCYTHE=ITEMS.register("moon_scythe",()->new MoonScythe(new Item.Properties().stacksTo(1).durability(500)));
    public static final RegistryObject<Item> MOONSTONE_HELMET = ITEMS.register("moonstone_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.MOONSTONE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MOONSTONE_CHESTPLATE = ITEMS.register("moonstone_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.MOONSTONE, ArmorItem.Type.CHESTPLATE, new Item.Properties(),1));
    public static final RegistryObject<Item> MOONSTONE_LEGGINGS = ITEMS.register("moonstone_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.MOONSTONE, ArmorItem.Type.LEGGINGS, new Item.Properties(),1));
    public static final RegistryObject<Item> MOONSTONE_BOOTS = ITEMS.register("moonstone_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.MOONSTONE, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LUNAR_DIAL = ITEMS.register("lunar_dial", ()->new LunarDial(new Item.Properties().defaultDurability(1000)));

    public static final RegistryObject<Item> EBONY_FUR = ITEMS.register("ebony_fur", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_HELMET = ITEMS.register("umbra_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.EBONY_FUR, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_CHESTPLATE = ITEMS.register("umbra_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.EBONY_FUR, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_LEGGINGS = ITEMS.register("umbra_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.EBONY_FUR, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> UMBRA_BOOTS = ITEMS.register("umbra_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.EBONY_FUR, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> SUNSTONE = ITEMS.register("sunstone", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLUORESCENT_BOW=ITEMS.register("fluorescent_bow",()->new FluorescentBow(new Item.Properties().stacksTo(1).durability(448)));
    public static final RegistryObject<Item> RADIANT_HELMET = ITEMS.register("radiant_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.SUNSTONE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_CHESTPLATE = ITEMS.register("radiant_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.SUNSTONE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_LEGGINGS = ITEMS.register("radiant_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.SUNSTONE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_BOOTS = ITEMS.register("radiant_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.SUNSTONE, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> DYING_LIGHT_ESSENCE =
            ITEMS.register("dying_light_essence", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_HELMET = ITEMS.register("remnant_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.DYING_LIGHT_ESSENCE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_CHESTPLATE = ITEMS.register("remnant_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.DYING_LIGHT_ESSENCE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_LEGGINGS = ITEMS.register("remnant_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.DYING_LIGHT_ESSENCE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> REMNANT_BOOTS = ITEMS.register("remnant_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.DYING_LIGHT_ESSENCE, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> LUNA_TOME=ITEMS.register("luna_tome",()->new LunaTomeItem(new Item.Properties()));

    public static final RegistryObject<Item> MIDNIGHT_IRON_INGOT = ITEMS.register("midnight_iron_ingot", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_SWORD=ITEMS.register("midnight_iron_sword",()->new SwordItem(CustomTiers.MIDNIGHT_IRON,3,-2.f,new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_PICKAXE=ITEMS.register("midnight_iron_pickaxe",()->new PickaxeItem(CustomTiers.MIDNIGHT_IRON,1, -2.8F,new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_AXE=ITEMS.register("midnight_iron_axe",()->new AxeItem(CustomTiers.MIDNIGHT_IRON,5.5F, -3.0F,new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_SHOVEL=ITEMS.register("midnight_iron_shovel",()->new ShovelItem(CustomTiers.MIDNIGHT_IRON,1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MIDNIGHT_IRON_HOE=ITEMS.register("midnight_iron_hoe",()->new HoeItem(CustomTiers.MIDNIGHT_IRON,-2, -0.7F,new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_HELMET = ITEMS.register("knightmare_helmet",
            () -> new CustomArmorItem(CustomArmorMaterial.MIDNIGHT_IRON, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_CHESTPLATE = ITEMS.register("knightmare_chestplate",
            () -> new CustomArmorItem(CustomArmorMaterial.MIDNIGHT_IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_LEGGINGS = ITEMS.register("knightmare_leggings",
            () -> new CustomArmorItem(CustomArmorMaterial.MIDNIGHT_IRON, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTMARE_BOOTS = ITEMS.register("knightmare_boots",
            () -> new CustomArmorItem(CustomArmorMaterial.MIDNIGHT_IRON, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> EVERLIGHT_HELMET=ITEMS.register("everlight_helmet",
            ()->new CustomArmorItem(CustomArmorMaterial.CONCENTRATED_LIGHT_ESSENCE,ArmorItem.Type.HELMET,new Item.Properties(),3));
    public static final RegistryObject<Item> EVERLIGHT_CHESTPLATE=ITEMS.register("everlight_chestplate",
            ()->new CustomArmorItem(CustomArmorMaterial.CONCENTRATED_LIGHT_ESSENCE,ArmorItem.Type.CHESTPLATE,new Item.Properties(),2));
    public static final RegistryObject<Item> EVERLIGHT_LEGGINGS=ITEMS.register("everlight_leggings",
            ()->new CustomArmorItem(CustomArmorMaterial.CONCENTRATED_LIGHT_ESSENCE,ArmorItem.Type.LEGGINGS,new Item.Properties(),2));
    public static final RegistryObject<Item> EVERLIGHT_BOOTS=ITEMS.register("everlight_boots",
            ()->new CustomArmorItem(CustomArmorMaterial.CONCENTRATED_LIGHT_ESSENCE,ArmorItem.Type.BOOTS,new Item.Properties(),3));

    public static final RegistryObject<Item> COSMIC_HIDE =
            ITEMS.register("cosmic_hide", () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> PULSATING_DARKNESS =
            ITEMS.register("pulsating_darkness", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHADE_CAP=
            ITEMS.register("shade_helmet",()->new CustomArmorItem(CustomArmorMaterial.PULSATING_DARKNESS,ArmorItem.Type.HELMET,new Item.Properties(),1));
    public static final RegistryObject<Item> SHADE_CLOAK=
            ITEMS.register("shade_chestplate",()->new CustomArmorItem(CustomArmorMaterial.PULSATING_DARKNESS,ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> SHADE_PANTS=
            ITEMS.register("shade_leggings",()->new CustomArmorItem(CustomArmorMaterial.PULSATING_DARKNESS,ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> SHADE_SLIPPERS=
            ITEMS.register("shade_boots",()->new CustomArmorItem(CustomArmorMaterial.PULSATING_DARKNESS,ArmorItem.Type.BOOTS,new Item.Properties(),1));

    public static final RegistryObject<Item> THREAD_OF_INSANITY =
            ITEMS.register("thread_of_insanity", () ->new Item(new Item.Properties()));



}
