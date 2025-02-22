package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
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

import java.util.List;

public class RemnantArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    String abilityFLCD="Remnant_FinalLight_CD";
    String abilityCollapse="Remnant_Collapse";

    int[] devouringLightFoodLevels={1,1,1,2};
    float[] devouringLightSaturationLevels={0.5f,0.75f,1f,1f};

    public static final String DL_NAME = "tooltip.celestial_awakening.remnant_armor.dl_name";
    public static final String DL_DESC = "tooltip.celestial_awakening.remnant_armor.dl_desc";
    public static final String FL_NAME = "tooltip.celestial_awakening.remnant_armor.fl_name";
    public static final String FL_DESC = "tooltip.celestial_awakening.remnant_armor.fl_desc";
    public static final String COLLAPSE_NAME = "tooltip.celestial_awakening.remnant_armor.collapse_name";
    public static final String COLLAPSE_DESC = "tooltip.celestial_awakening.remnant_armor.collapse_desc";


    @Override
    public void onLivingDeath(LivingDeathEvent event,Player player,int cnt){
        devouringLight(event,player,cnt);
    }
    @Override
    public void onLivingHurtOthers(LivingHurtEvent event,Player player,int cnt){
        if (cnt==4){
            collapse(event,player);
        }

    }
    @Override
    public void onLivingDamageSelf(LivingDamageEvent event,Player player,int cnt){
        if (cnt==4){
            finalLight(player,event.getAmount());
        }

    }
    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,FL_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,COLLAPSE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,DL_NAME,boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,FL_NAME,boldColor,FL_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,COLLAPSE_NAME,boldColor,COLLAPSE_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,DL_NAME,boldColor,DL_DESC,new Object[]{devouringLightFoodLevels[cnt-1],devouringLightSaturationLevels[cnt-1]},infoColor);
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
        LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null && cap.getAbilityCD(abilityFLCD)==null&& player.getHealth()-amt<=0.2f*player.getMaxHealth()){
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,5,1));
            player.addEffect(new MobEffectInstance(MobEffectInit.REMNANT_FL.get(),5,1,false,false,false));
            cap.insertIntoAbilityMap(abilityFLCD,20*120);
        }

    }

    public void collapse(LivingHurtEvent event,Player player){
        LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
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
