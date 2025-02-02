package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.util.CA_UUIDs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class LatticeArmor extends ArmorEffect{

    private static AttributeModifier glacierModifier=new AttributeModifier(CA_UUIDs.latticeGlacierID,"Lattice Attack Modifier",0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    //                DamageSourceNoIFrames source=new DamageSourceNoIFrames(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CustomDamageTypes.ECHO));
    //                source.invulTicks=target.invulnerableTime;


    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (event instanceof LivingEquipmentChangeEvent){
            glacierAttackMod(player,cnt);
            pressure(player,cnt);
        }
        if (cnt==4){
            if (event instanceof TickEvent.PlayerTickEvent){
                glacier(player);
                frostburn(player);
            }
            else if (event instanceof LivingDamageEvent){
                glacierSlow(player, (LivingDamageEvent) event);
            }
        }

    }

    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {

    }
    private void frostburn(Player player){//tick
        //DamageSource source= CustomDamageSources
        //AABB aabb
    }

    private void glacier(Player player){//needs to use a tick event
        AABB aabb=player.getBoundingBox();
        TargetingConditions conds=TargetingConditions.forCombat();
        List<LivingEntity> livingEntityList =player.level().getNearbyEntities(LivingEntity.class,conds,player,aabb);
        DamageSource source=new DamageSource
                (player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.CRAMMING),
                        player);
        float dmgAmt=(float)(2 + player.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()/3f +player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue()/0.5f);
        for (LivingEntity livingEntity:livingEntityList) {
            livingEntity.hurt(source,dmgAmt);
        }
    }
    private void glacierSlow(Player player, LivingDamageEvent event){
        if (event.getSource().getDirectEntity()==player){
            LivingEntity livingEntity= event.getEntity();
            //should be a pot effect
        }
    }

    private void glacierAttackMod(Player player, int cnt){//equipment change
        if (cnt==4){
            player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(glacierModifier);
        }
        else{
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(glacierModifier);
        }
    }

    private void pressure(Player player, int cnt){//on equipment change
        float kbRes=0;
        int toughnessBoost=0;
        if (cnt>0){
            kbRes=0.15f*cnt;
            toughnessBoost= (int) Math.pow(2,cnt);
        }

        AttributeModifier pressureKBRes=new AttributeModifier(CA_UUIDs.latticePressureID,"Lattice KB Res",kbRes, AttributeModifier.Operation.ADDITION);
        if (player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getModifier(CA_UUIDs.latticePressureID)!=null){
            player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(CA_UUIDs.latticePressureID);
        }
        AttributeModifier pressureToughBoost=new AttributeModifier(CA_UUIDs.latticePressureID,"Lattice Toughness",toughnessBoost, AttributeModifier.Operation.ADDITION);
        if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).getModifier(CA_UUIDs.latticePressureID)!=null){
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(CA_UUIDs.latticePressureID);
        }
        player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addTransientModifier(pressureKBRes);
        player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(pressureToughBoost);
    }
}
