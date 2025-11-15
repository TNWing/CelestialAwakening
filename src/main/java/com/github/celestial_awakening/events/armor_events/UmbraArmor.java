package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UmbraArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    String abilityDread="Umbra_Dread";
    String abilityPursuit="Umbra_Pursuit";
    String abilityCursedLight="Umbra_CL";
    float[] dreadVals={0.4f,0.6f,0.9f,1.3f};
    int[] dreadCD={600,500,400,300};
    int[] dreadCDReduction={20,40,60,80};

    public static final String LORE="tooltip.celestial_awakening.umbra_armor.lore";
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
        }
    }
    @Override
    public void onLivingHurtSelf(LivingHurtEvent event,Player player,int cnt){
        if (cnt==4){
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
        event.getToolTip().add(Component.translatable(LORE));
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,PURSUIT_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,CL_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,DREAD_NAME,boldColor);
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
        ToolTipBuilder.addFullArmorSetComponent(event,PURSUIT_NAME,boldColor,
                PURSUIT_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,CL_NAME,boldColor,
                CL_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,DREAD_NAME,boldColor,
              DREAD_DESC,new Object[]{dreadVals[cnt-1],dreadCD[cnt-1]/20,dreadCDReduction[cnt-1]/20}
                ,infoColor);
        event.getToolTip().addAll(savedToolTip);
    }
    public void dread(LivingHurtEvent event,Player player,int cnt){
        if (cnt>0){
            LivingEntity entity=event.getEntity();
            if (entity.getHealth()>=entity.getMaxHealth()){
                @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.capability);
                capOptional.ifPresent(cap->{
                    if (cap.getAbilityCD(abilityDread)==null){
                        event.setAmount(event.getAmount()+dreadVals[cnt-1]);
                        cap.insertIntoAbilityMap(abilityDread,dreadCD[cnt-1]);
                    }
                });


            }
        }

    }
/*
pursuit
hitting an enemy below 50% max HP will grant a speed boost to the player.
CD of 15 seconds, resets on kill
 */
    public void cdModifier(LivingDeathEvent event,Player player, int cnt){
        @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            if (cap.getAbilityCD(abilityDread)!=null){
                cap.changeAbilityCD(abilityDread,-20*cnt);
            }
            if (cnt==4 && cap.getAbilityCD(abilityPursuit)!=null){
                cap.changeAbilityCD(abilityPursuit,-300);
            }
        });
    }

    public void pursuit(LivingHurtEvent event,Player player){
        LivingEntity target=event.getEntity();
        @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            if(cap.getAbilityCD(abilityPursuit)==null && event.getSource().getEntity() == player){
                if (target.getHealth()-event.getAmount()<0.5f*target.getMaxHealth()){
                    MobEffectInstance spdBoost=new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,2);
                    player.addEffect(spdBoost);
                    cap.insertIntoAbilityMap(abilityPursuit,300);
                }
            }
        });

    }
/*
Getting hit will apply Weakness 2 for 3 seconds to the attacker
CD of 8 seconds
 */
    public void cursedLight(LivingHurtEvent event,Player player){
        Entity entity=event.getSource().getEntity();
        if (entity instanceof LivingEntity && event.getEntity()==player){
            @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.capability);
            capOptional.ifPresent(cap->{
                if (cap.getAbilityCD(abilityCursedLight)==null){
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS,60,1));
                    AABB aabb=new AABB(player.position(),player.position());
                    aabb=aabb.inflate(3f,1,3);
                    List<LivingEntity> entities=player.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnorePlayers_Allies_Passive_Predicate(player));
                    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),player);
                    for (LivingEntity living:entities) {
                        living.hurt(source,1f);
                        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,30));
                    }
                    cap.insertIntoAbilityMap(abilityCursedLight,160);
                }
            });

        }
    }
}
