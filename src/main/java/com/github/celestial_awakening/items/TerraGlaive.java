package com.github.celestial_awakening.items;

import com.github.celestial_awakening.capabilities.SunStaffCapability;
import com.github.celestial_awakening.capabilities.SunStaffCapabilityProvider;
import com.github.celestial_awakening.capabilities.TerraGlaiveCapability;
import com.github.celestial_awakening.capabilities.TerraGlaiveCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.projectile.GenericShard;
import com.github.celestial_awakening.util.MathFuncs;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        AtomicInteger abilityCastType= new AtomicInteger();
        abilityCastType.set(1);
        if (!level.isClientSide) {
            @NotNull LazyOptional<TerraGlaiveCapability> capOptional=itemStack.getCapability(TerraGlaiveCapabilityProvider.cap);
            capOptional.ifPresent(cap->{
                if (cap.getSpearCD()==0){
                    cap.setSpearCD((short) 100);
                    Vec3 dir=player.getLookAngle();
                    float hAng= (float) ((Math.toDegrees (Math.atan2(dir.z,dir.x))));
                    float vAng=MathFuncs.getVertAngFromVec(dir);
                    DamageSourceIgnoreIFrames spear=new DamageSourceIgnoreIFrames(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FALLING_STALACTITE),player);

                    GenericShard earthShard=GenericShard.create(level, GenericShard.Type.EARTH,120,8.4f,hAng,vAng,2.5f,spear,player,false);
                    earthShard.setPos(player.position());
                    level.addFreshEntity(earthShard);
                    itemStack.hurtAndBreak(2,player,(p_40992_) -> {p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);});

                }
            });
        }
        switch (abilityCastType.get()){
            case 1:{
                return InteractionResultHolder.consume(itemStack);
            }
            case 2:{
                player.startUsingItem(interactionHand);
                return InteractionResultHolder.consume(itemStack);
            }
            default:{
                return InteractionResultHolder.fail(itemStack);//failed to use either ability
            }
        }
    }
    public TerraGlaive(Properties p_41383_,float dmg,float atkSpd) {
        super(p_41383_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", dmg, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",atkSpd, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",0.5f, AttributeModifier.Operation.ADDITION));

        atkDmg=dmg;
        this.defaultModifiers = builder.build();

    }
    public void inventoryTick(ItemStack itemStack, Level level, Entity holder, int vanillaIndex, boolean isSelectedIndex) {
        if (!level.isClientSide()){
            @NotNull LazyOptional<TerraGlaiveCapability> capOptional=itemStack.getCapability(TerraGlaiveCapabilityProvider.cap);
            capOptional.ifPresent(cap->{
                cap.decrementCD();
            });
        }
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
