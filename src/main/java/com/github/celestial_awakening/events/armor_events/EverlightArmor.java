package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.EntityFuncs;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class EverlightArmor extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    String abilityStarGazer="Everlight_SG";
    String abilityPhotonCycle="Everlight_PC";
    public static final String UNYIELDING_NAME = "tooltip.celestial_awakening.everlight_armor.unyielding_name";
    public static final String UNYIELDING_DESC = "tooltip.celestial_awakening.everlight_armor.unyielding_desc";
    public static final String STAR_GAZER_NAME = "tooltip.celestial_awakening.everlight_armor.star_gazer_name";
    public static final String STAR_GAZER_DESC = "tooltip.celestial_awakening.everlight_armor.star_gazer_desc";
    public static final String PHOTON_CYCLE_NAME = "tooltip.celestial_awakening.everlight_armor.photon_cycle_name";
    public static final String PHOTON_CYCLE_DESC = "tooltip.celestial_awakening.everlight_armor.photon_cycle_desc";
    MobEffectInstance glow=new MobEffectInstance(MobEffects.GLOWING,120);
    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event,Player player, int cnt){
        if (cnt==4){
            if (EntityFuncs.isInCombat(player)){
                photonCycle(player);
            }

        }
    }
    @Override
    public void onLivingDeath(LivingDeathEvent event,Player player, int cnt){
        unyieldingFlame(event,player,cnt);
    }

    @Override
    public void onLivingDamageSelf(LivingDamageEvent event,Player player, int cnt){
        if (cnt==4){
            starGazer(event,player);
        }
    }
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
        ToolTipBuilder.addFullSetName(event,STAR_GAZER_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,PHOTON_CYCLE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,UNYIELDING_NAME,boldColor);
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
        ToolTipBuilder.addFullArmorSetComponent(event,STAR_GAZER_NAME,boldColor,STAR_GAZER_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,PHOTON_CYCLE_NAME,boldColor, PHOTON_CYCLE_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,UNYIELDING_NAME,boldColor,UNYIELDING_DESC,infoColor);
        event.getToolTip().addAll(savedToolTip);
    }

    private void unyieldingFlame(LivingDeathEvent event,Player player,int cnt){
        int dura=50+cnt*10;//60 ticks to 90 ticks, or 3 sec to 4.5 sec
        int lvl= (int) (Math.ceil(cnt/2f));
        MobEffectInstance absorption=new MobEffectInstance(MobEffects.ABSORPTION,dura,lvl-1);
        player.addEffect(absorption);
        AABB aabb=player.getBoundingBox();
        aabb=aabb.inflate(4f);
        List<LivingEntity> entities=player.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate(player));
        int size=entities.size();
        if (!entities.isEmpty() && size>0){
            Random rand=new Random();
            int r=rand.nextInt(size);
            LivingEntity e=entities.remove(r);
            e.setSecondsOnFire(3+lvl);
            size--;
            if (size>0){
                r=rand.nextInt(size);
                e=entities.remove(r);
                e.setSecondsOnFire(3+lvl);
            }
        }
    }

    private void photonCycle(Player player){
        @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.playerCapability);
        capOptional.ifPresent(cap->{
            if (cap.getAbilityCD(abilityPhotonCycle)==null){
                AABB aabb=player.getBoundingBox();
                aabb=aabb.inflate(6.5f,2,6.5f);
                List<LivingEntity> entities=player.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnorePlayers_Allies_Passive_Predicate(player));
                if (!entities.isEmpty()){
                    if (player.getHealth()>player.getMaxHealth()*0.5f){
                        DamageSourceIgnoreIFrames fireSource=new DamageSourceIgnoreIFrames(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.ON_FIRE),player);
                        for (LivingEntity livingEntity:entities) {
                            livingEntity.setSecondsOnFire(6);
                            livingEntity.hurt(fireSource,2.5f);
                        }
                        cap.insertIntoAbilityMap(abilityPhotonCycle,240);
                    }
                    else{
                        float totalVal=0;
                        for (LivingEntity livingEntity:entities) {
                            int fireTimeTaken=0;
                            int glowTimeTaken=0;
                            int remainingFireTime;
                            if (livingEntity.isOnFire()){
                                remainingFireTime=livingEntity.getRemainingFireTicks();
                                if (remainingFireTime>=20){
                                    fireTimeTaken=20;
                                    remainingFireTime-=20;
                                }
                                else{
                                    fireTimeTaken=remainingFireTime;
                                    remainingFireTime=0;
                                }
                                livingEntity.setRemainingFireTicks(remainingFireTime);
                            }
                            if (livingEntity.hasEffect(MobEffects.GLOWING)){
                                MobEffectInstance glowInstance=livingEntity.getEffect(MobEffects.GLOWING);
                                int newDura=glowInstance.getDuration();
                                if (newDura>20){
                                    glowTimeTaken=20;
                                    newDura-=20;
                                    //maybe just use reflection to modify the dura?
                                    //dura:f_19503_

                                    ObfuscationReflectionHelper.setPrivateValue(MobEffectInstance.class,glowInstance,newDura,"f_19503_");
                                }
                                else{
                                    glowTimeTaken=newDura;
                                    livingEntity.removeEffect(MobEffects.GLOWING);
                                }
                            }
                            totalVal+=fireTimeTaken*0.6f + glowTimeTaken*0.9f;
                        }

                        if (totalVal>0){
                            player.heal(totalVal*0.05f);
                            cap.insertIntoAbilityMap(abilityPhotonCycle,240);
                        }
                        //so at most, for each entity, a player can heal 1.5 hp
                    }

                }
            }
        });
    }
    //slap a cd on the glow applying
    private void starGazer(LivingDamageEvent event,Player player){
        /*
        @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.playerCapability);
        capOptional.ifPresent(cap->{
            if (cap.getAbilityCD(abilityStarGazer)==null){

            }
        });

         */
        if (event.getSource().getEntity() instanceof LivingEntity){
            LivingEntity attacker= (LivingEntity) event.getSource().getEntity();
            attacker.addEffect(glow);
            if (!attacker.isCurrentlyGlowing()){
                player.heal(0.2f);
            }
        }
        float dmgMult=1f;
        Predicate<LivingEntity> getGlowing= livingEntity -> livingEntity.hasEffect(MobEffects.GLOWING);
        AABB bounds=new AABB(player.position().subtract(new Vec3(6,2,6)),player.position().add(new Vec3(6,2,6)));
        TargetingConditions conds=TargetingConditions.forCombat();
        conds.selector(getGlowing);
        conds.range(6);
        int glowingEnemies=player.level().getNearbyEntities(LivingEntity.class, conds,player,bounds).size();
        dmgMult-=glowingEnemies*0.05f;
        if (dmgMult<0.5f){
            dmgMult=0.5f;
        }
        event.setAmount(event.getAmount()*dmgMult);

    }
}
