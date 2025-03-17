package com.github.celestial_awakening.items;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.Util;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.EnumMap;
import java.util.UUID;

public class CustomArmorItem extends ArmorItem {
    private static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    private final float toughnessModifier;
    public CustomArmorItem(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
        toughnessModifier=0;
    }

    public CustomArmorItem(ArmorMaterial material, ArmorItem.Type type, Item.Properties properties, float toughnessModifier) {
        super(material, type, properties.defaultDurability(material.getDurabilityForType(type)));
        this.toughnessModifier = toughnessModifier;

        // Create your own builder
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier",  this.getDefense(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness",this.getToughness(), AttributeModifier.Operation.ADDITION));
        if (this.getKnockbackResistance() > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance",this.getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
        }
        if (this.getSpdBoost()>0){
            builder.put(Attributes.MOVEMENT_SPEED,new AttributeModifier(uuid,"Armor Speed",this.getSpdBoost(), AttributeModifier.Operation.ADDITION));
        }
        //	defaultModifiers f_40383_
        ObfuscationReflectionHelper.setPrivateValue(ArmorItem.class,this,builder.build(),"f_40383_");
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }
    @Override
    public float getToughness() {
        return this.material.getToughness() + this.toughnessModifier;
    }

    public float getKnockbackResistance() {
        return this.material.getKnockbackResistance();
    }

    public float getSpdBoost() {
        return ((CustomArmorMaterial)this.material).getSpdBoost();
    }
}
