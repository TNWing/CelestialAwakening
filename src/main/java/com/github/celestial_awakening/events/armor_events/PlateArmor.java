package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlateArmor extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    public static final String WARDING_NAME = "tooltip.celestial_awakening.plate_armor.warding_name";
    public static final String WARDING_DESC = "tooltip.celestial_awakening.plate_armor.warding_desc";
    public static final String DROPSHOCK_NAME = "tooltip.celestial_awakening.plate_armor.dropshock_name";
    public static final String DROPSHOCK_DESC = "tooltip.celestial_awakening.plate_armor.dropshock_desc";
    String abilityDropShock="Plate_DS";
    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        List<Component> savedToolTip=new ArrayList<>();
        Component name=null;
        for (Component c:event.getToolTip()){
            if (c.getString().equals(event.getItemStack().getHoverName().getString())){
                name=c;
                continue;
            }
            savedToolTip.add(c.copy());
        }
        event.getToolTip().clear();
        event.getToolTip().add(name);
        ToolTipBuilder.addShiftInfo(event);
        //ToolTipBuilder.addFullSetName(event, SS_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,DROPSHOCK_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,WARDING_NAME,boldColor);
        event.getToolTip().addAll(savedToolTip);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        List<Component> savedToolTip=new ArrayList<>();
        Component name=null;
        for (Component c:event.getToolTip()){
            if (c.getString().equals(event.getItemStack().getHoverName().getString())){
                name=c;
                continue;
            }
            savedToolTip.add(c.copy());
        }
        event.getToolTip().clear();
        event.getToolTip().add(name);
        //ToolTipBuilder.addFullArmorSetComponent(event,SS_NAME,boldColor,SS_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,DROPSHOCK_NAME,boldColor,DROPSHOCK_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,WARDING_NAME,boldColor,
                WARDING_DESC,new Object[]{cnt*5},
                infoColor);
        event.getToolTip().addAll(savedToolTip);
    }


    @Override
    public void onLivingHurtSelf(LivingHurtEvent event,Player player,int cnt){
        if (cnt==4 && event.getSource().is(DamageTypeTags.IS_FALL)){
            dropShock(event);
            System.out.println("TRIGGER DS");
        }
    }

    @Override
    public void onLivingDamageSelf(LivingDamageEvent event,Player player,int cnt){
        warding(event,cnt);
        System.out.println("TRIGGER WARD");
    }
    void warding(LivingDamageEvent event, int amt){
        LivingEntity livingEntity=event.getEntity();
        if (livingEntity.getMaxHealth()==livingEntity.getHealth()){
            event.setAmount(event.getAmount()*(1f-amt*0.05f));
        }
    }
    /*
    //i could post my own event and trigger it off of livingfall, or inject it into the livingentity fall damage method for better accuracy
    void dropShockEvent(LivingFallEvent event){
        float dist=event.getDistance();
        float dmgMult=event.getDamageMultiplier();
    }
    3
     */
    void dropShock(LivingHurtEvent event){//should already know dmg is falling

        LivingEntity livingEntity=event.getEntity();
        @NotNull LazyOptional<LivingEntityCapability> capOptional=livingEntity.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            float amt=event.getAmount();
            if(cap.getAbilityCD(abilityDropShock)==null && amt>1){
                System.out.println("DS TRIGGER");
                cap.insertIntoAbilityMap(abilityDropShock,150);
                event.setAmount(0);
                TargetingConditions conds=TargetingConditions.forCombat();
                conds.range(6);
                AABB bounds=new AABB(livingEntity.position().subtract(new Vec3(6,2,6)),livingEntity.position().add(new Vec3(6,2,6)));
                List<LivingEntity> livingEntityList=livingEntity.level().getNearbyEntities(LivingEntity.class, conds,livingEntity,bounds);
                for (LivingEntity entity:livingEntityList) {
                    System.out.println("hhurt " + entity + " FOR DMG " + amt*0.6f);
                    entity.hurt(livingEntity.level().damageSources().fall(),amt*0.6f);
                }
                System.out.println("NEW EVENT AMT IS " + event.getAmount());
                event.setCanceled(true);
            }
        });


    }
}
