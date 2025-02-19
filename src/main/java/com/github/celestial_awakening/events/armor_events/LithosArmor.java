package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class LithosArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    String abilityImpact="Lithos_Impact";

    String abilityMeteor="Lithos_Meteor";
    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (event instanceof LivingHurtEvent){
            if (cnt==4){
                livingMeteor((LivingHurtEvent) event,player);
                impact((LivingHurtEvent) event,player);
            }
            shatterSkin((LivingHurtEvent) event,player,cnt);
        }
    }
/*
Set Bonus
Impact
Crit attacks will deal area damage
CD: 5 sec
Living Meteor
Taking fall damage will apply slowness and weakness to nearby enemies as well as deal % of their current HP as true damage
Piece Bonus
Shatter-Skin
AoE version of thorns
CD: 1.5 sec

 */
    public void shatterSkin(LivingHurtEvent event,Player player, int cnt){

    }


    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,"Impact",boldColor);
        ToolTipBuilder.addFullSetName(event,"Living Meteor",boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,"Impact",boldColor,"Upon falling below 20% of your max HP, gain increased attack damage, attack speed, and damage reduction for 5 seconds. Cooldown of 2 minutes",infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,"Living Meteor",boldColor,"Landing a critical hit will deal 20% of the damage dealt to surrounding enemies. Cooldown of 5 seconds",infoColor);
    }
    public void impact(LivingHurtEvent event,Player player){
        if (event.getSource().getDirectEntity()==player){
            PlayerCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null && cap.getAbilityCD(abilityImpact)==null){
                AABB aabb=new AABB(player.position().subtract(new Vec3(1.2f,0,1.2f)),player.position().add(new Vec3(1.2f,0,1.2f)));
                List<LivingEntity> livingEntityList=player.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate(player));
                if (!livingEntityList.isEmpty()){
                    cap.insertIntoAbilityMap(abilityImpact,100);
                    for (LivingEntity entity:livingEntityList) {
                        DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FLY_INTO_WALL),player);
                        entity.hurt(source,event.getAmount()*0.25f);
                    }
                }

            }
        }
    }

    public void livingMeteor(LivingHurtEvent event,Player player){
        if (event.getSource().is(DamageTypeTags.IS_FALL) && event.getEntity() == player){
            PlayerCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null && cap.getAbilityCD(abilityMeteor)==null){
                AABB aabb=new AABB(player.position().subtract(new Vec3(3.5f,0,3.5f)),player.position().add(new Vec3(3.5f,0,3.5f)));
                List<LivingEntity> livingEntityList=player.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate(player));
                for (LivingEntity entity:livingEntityList) {
                    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FLY_INTO_WALL),player);
                    entity.hurt(source,event.getAmount()*1.25f);
                }
                cap.insertIntoAbilityMap(abilityMeteor,900);
            }
        }
    }
}
