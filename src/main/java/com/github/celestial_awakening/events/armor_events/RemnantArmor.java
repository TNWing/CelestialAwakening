package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class RemnantArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    String abilityFLCD="Remnant_FinalLight_CD";
    String abilityCollapse="Remnant_Collapse";

    int[] devouringLightFoodLevels={1,1,1,2};
    float[] devouringLightSaturationLevels={0.5f,0.75f,1f,1f};

    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (event instanceof LivingDeathEvent){
            devouringLight((LivingDeathEvent) event,player,cnt);
        }
        else if (event instanceof ItemTooltipEvent){
            onItemTooltipEvent((ItemTooltipEvent) event,cnt);
        }
        else if (cnt==4){
            if (event instanceof LivingHurtEvent){
                LivingHurtEvent hurtEvent= (LivingHurtEvent) event;
                if (hurtEvent.getSource().getDirectEntity() == player){
                    collapse(hurtEvent,player);
                }
            }
            else if (event instanceof LivingDamageEvent){
                if (((LivingDamageEvent) event).getEntity() ==player){

                    finalLight(player,((LivingDamageEvent) event).getAmount());
                }
            }
        }

    }



    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,"Final Light",boldColor);
        ToolTipBuilder.addFullSetName(event,"Collapse",boldColor);
        ToolTipBuilder.addPieceBonusName(event,"Devouring Light",boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,"Final Light",boldColor,"Upon falling below 20% of your max HP, gain increased attack damage, attack speed, and damage reduction for 5 seconds. Cooldown of 2 minutes",infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,"Collapse",boldColor,"Landing a critical hit will deal 20% of the damage dealt to surrounding enemies. Cooldown of 5 seconds",infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,"Devouring Light",boldColor,String.format("Kills restore %s hunger and %s saturation",devouringLightFoodLevels[cnt-1],devouringLightSaturationLevels[cnt-1]),infoColor);
    }

    /*
    Kills restore hunger
     */
    public void devouringLight(LivingDeathEvent event,Player player,int cnt){
        if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity()==player){
            if (cnt>0){
                FoodData foodData=player.getFoodData();
                foodData.eat(devouringLightFoodLevels[cnt-1],devouringLightSaturationLevels[cnt-1]);
            }
        }

    }
    public void finalLight(Player player,float amt){
        LivingEntityCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null && cap.getAbilityCD(abilityFLCD)==null&& player.getHealth()-amt<=0.2f*player.getMaxHealth()){
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,5,1));
            player.addEffect(new MobEffectInstance(MobEffectInit.REMNANT_FL.get(),5,1,false,false,false));
        }

    }

    public void collapse(LivingHurtEvent event,Player player){
        LivingEntityCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null && cap.getAbilityCD(abilityCollapse) ==null && event.getSource().getDirectEntity()==player){//off CD
            Level level=event.getEntity().level();
            LivingEntity target=event.getEntity();
            Vec3 targetPos=target.position();
            AABB aabb=new AABB(targetPos.subtract(new Vec3(2.5f,2.5f,2.5f)),targetPos.add(new Vec3(2.5f,2.5f,2.5f)));
            List<LivingEntity> entities= level.getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate(player));
            if (!entities.isEmpty()){
                cap.insertIntoAbilityMap(abilityCollapse,300);
                for (LivingEntity livingEntity:entities) {
                    livingEntity.hurt(event.getSource(),1.25f);
                    Vec3 dir= MathFuncs.getDirVecNoNormalize(targetPos,livingEntity.position()).scale(0.7f);
                    livingEntity.push(dir.x,dir.y,dir.z);
                }
            }


        }
    }
}
