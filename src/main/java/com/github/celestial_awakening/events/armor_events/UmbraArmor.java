package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class UmbraArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    String abilityDread="Umbra_Dread";
    String abilityPursuit="Umbra_Pursuit";
    String abilityCursedLight="Umbra_CL";
    float[] dreadVals={0.4f,0.6f,0.9f,1.3f};
    int[] dreadCD={600,500,400,300};
    int[] dreadCDReduction={20,40,60,80};

    public static final String DREAD_NAME = "tooltip.celestial_awakening.umbra_armor.dread_name";
    public static final String DREAD_DESC = "tooltip.celestial_awakening.umbra_armor.dread_desc";
    public static final String PURSUIT_NAME = "tooltip.celestial_awakening.umbra_armor.pursuit_name";
    public static final String PURSUIT_DESC = "tooltip.celestial_awakening.umbra_armor.pursuit_desc";
    public static final String CL_NAME = "tooltip.celestial_awakening.umbra_armor.cl_name";
    public static final String CL_DESC = "tooltip.celestial_awakening.umbra_armor.cl_desc";


    @Override
    public void onLivingDeath(LivingDeathEvent event,Player player,int cnt){
        cdModifier( event,player,cnt);
    }
    @Override
    public void onLivingHurtOthers(LivingHurtEvent event,Player player,int cnt){
        dread( event,player,cnt);
        if (cnt==4){
            pursuit(event,player);
            cursedLight(event,player);
        }
    }
/*
Growing Dread
Deal 0.4/0.6/0.9/1.3 extra base damage to a full HP target.
Cooldown: 30/25/20/15 seconds.
Reduces CD by 1/2/3/4 sec on kill
 */
    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,PURSUIT_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,CL_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,DREAD_NAME,boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,PURSUIT_NAME,boldColor,
                PURSUIT_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,CL_NAME,boldColor,
                CL_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,DREAD_NAME,boldColor,
              DREAD_DESC,new Object[]{dreadVals[cnt-1],dreadCD[cnt-1]/20,dreadCDReduction[cnt-1]/20}
                ,infoColor);
    }
    public void dread(LivingHurtEvent event,Player player,int cnt){
        if (cnt>0){
            LivingEntity entity=event.getEntity();
            if (entity.getHealth()>=entity.getMaxHealth()){
                LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
                if (cap!=null && cap.getAbilityCD(abilityDread)==null){
                    event.setAmount(event.getAmount()+dreadVals[cnt-1]);
                    cap.insertIntoAbilityMap(abilityDread,dreadCD[cnt-1]);
                }

            }
        }

    }
/*
pursuit
hitting an enemy below 50% max HP will grant a speed boost to the player.
CD of 15 seconds, resets on kill
 */
    public void cdModifier(LivingDeathEvent event,Player player, int cnt){
        LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null){
            if (cap.getAbilityCD(abilityDread)!=null){
                cap.changeAbilityCD(abilityDread,-20*cnt);
            }
            if (cnt==4 && cap.getAbilityCD(abilityPursuit)!=null){
                cap.changeAbilityCD(abilityPursuit,-300);
            }
        }
    }

    public void pursuit(LivingHurtEvent event,Player player){
        LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
        LivingEntity target=event.getEntity();
        if(cap!=null && cap.getAbilityCD(abilityPursuit)==null && event.getSource().getEntity() == player){
            if (target.getHealth()-event.getAmount()<0.5f*target.getMaxHealth()){
                MobEffectInstance spdBoost=new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,2);
                player.addEffect(spdBoost);
                cap.insertIntoAbilityMap(abilityPursuit,300);
            }
        }
    }
/*
Getting hit will apply Weakness 2 for 3 seconds to the attacker

CD of 8 seconds
 */
    public void cursedLight(LivingHurtEvent event,Player player){
        Entity entity=event.getSource().getEntity();
        if (entity instanceof LivingEntity && event.getEntity()==player){
            LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null && cap.getAbilityCD(abilityCursedLight)==null){
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS,60,2));
                cap.insertIntoAbilityMap(abilityCursedLight,160);
            }
        }
    }
}
