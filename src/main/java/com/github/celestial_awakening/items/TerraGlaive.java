package com.github.celestial_awakening.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.List;

public class TerraGlaive extends CustomItem{
    float atkDmg;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    /*

    Left Click: Rock Smash
Fully charged attacks will


Right Click: Earth Spear
Fires off a rock spear, piercing and dealing damage
Shift + Right Click: Infused Earth
Spear can consume stones to empower itself
Switches between what stones to consume
Scorched Stone: Ignites entities that are hit
Deepslate: Increased damage
Cobblestone

     */
    public TerraGlaive(Properties p_41383_,float dmg,float atkSpd) {
        super(p_41383_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", dmg, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",atkSpd, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",0.5f, AttributeModifier.Operation.ADDITION));

        atkDmg=dmg;
        this.defaultModifiers = builder.build();

    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        /*
        Component flashName=Component.translatable("tooltip.celestial_awakening.sun_staff.flash_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(flashName);
        Component flashButton=Component.translatable("tooltip.celestial_awakening.sun_staff.flash_control").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(flashButton);
        components.add(Component.translatable("tooltip.celestial_awakening.sun_staff.flash_desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));
        Component rayName=Component.translatable("tooltip.celestial_awakening.sun_staff.ray_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(rayName);
        Component rayButton=Component.translatable("tooltip.celestial_awakening.sun_staff.ray_control").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(rayButton);
        components.add(Component.translatable("tooltip.celestial_awakening.sun_staff.ray_desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));

         */
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }
}
