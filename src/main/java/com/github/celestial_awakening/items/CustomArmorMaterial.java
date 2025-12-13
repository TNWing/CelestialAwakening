package com.github.celestial_awakening.items;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.init.ItemInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum CustomArmorMaterial implements ArmorMaterial {
    LUNAR("moonstone", 20, new int[]{ 2, 3, 3, 2}, 12,
        SoundEvents.ARMOR_EQUIP_GENERIC, 0, 0f, () -> Ingredient.of(ItemInit.MOONSTONE.get())),

    RADIANT("radiant", 20, new int[]{ 2, 2, 2, 1}, 14,
            SoundEvents.ARMOR_EQUIP_LEATHER, 1, 0f, () -> Ingredient.of(ItemInit.SUNSTONE.get())),

    UMBRA("umbra",18,new int[]{2,5,4,1},12,SoundEvents.ARMOR_EQUIP_LEATHER,
            0,0f,()->Ingredient.of(ItemInit.ONYX_FUR.get())),

    REMNANT("remnant",18,new int[]{2,5,5,2},12,SoundEvents.ARMOR_EQUIP_CHAIN,
            0,0.1f,()->Ingredient.of(ItemInit.DYING_LIGHT_ESSENCE.get())),

    EVERLIGHT("everlight",25,new int[]{3,6,6,3},15,SoundEvents.ARMOR_EQUIP_CHAIN,
            2f,0.1f,()->Ingredient.of(ItemInit.DYING_LIGHT_ESSENCE.get()),0,1.5f),

    KNIGHTMARE("knightmare",22,new int[]{3,6,5,3},14,SoundEvents.ARMOR_EQUIP_IRON,
            2,0.1f,()->Ingredient.of(ItemInit.MIDNIGHT_IRON_INGOT.get())),

    STELLAR("stellar",19,new int[]{0,1,1,0},16,SoundEvents.ARMOR_EQUIP_ELYTRA,
            0,0f,()->Ingredient.of(ItemInit.COSMIC_HIDE.get())),

    SHADE("shade",19,new int[]{0,1,1,0},15,SoundEvents.ARMOR_EQUIP_ELYTRA,
            0,0f,()->Ingredient.of(ItemInit.PULSATING_DARKNESS.get()),0.025f,0),

    SCORCHED("scorched",19,new int[]{2,4,4,2},11,SoundEvents.STONE_BREAK,0.5f,0f,()->Ingredient.of(ItemInit.SCORCHED_STONE.get())),

    PLATE("plate",22,new int[]{2,7,6,2},13,SoundEvents.ARMOR_EQUIP_IRON,1,0.05f,()->Ingredient.of((ItemInit.GAIA_PLATE.get())));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float knockbackResistance;
    private final float toughness;
    private final Supplier<Ingredient> repairIngredient;
    private static final int[] BASE_DURABILITY = { 11, 16, 16, 13 };
    private final float spdBoost;
    private final float hpBoost;
    CustomArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound,
                        float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
        this.spdBoost=0;
        this.hpBoost=0;
    }
    CustomArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound,
                        float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient,float spd,float hp) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
        this.spdBoost=spd;
        this.hpBoost=hp;
    }
    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return BASE_DURABILITY[pType.ordinal()] * this.durabilityMultiplier;

    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionAmounts[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return CelestialAwakening.MODID + ":" + this.name;
    }

    @Override
    public float getToughness(){
        return this.toughness;
    }
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public float getSpdBoost(){
        return this.spdBoost;
    }
    public float getHpBoost(){
        return this.hpBoost;
    }
}
