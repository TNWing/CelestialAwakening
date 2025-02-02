package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class EverlightArmor extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (event instanceof ItemTooltipEvent){
            onItemTooltipEvent((ItemTooltipEvent) event,cnt);
        }
        else if (event instanceof LivingDeathEvent){
            pieceEffect(player,cnt);
        }
        else if (cnt==4){
            if (event instanceof TickEvent.PlayerTickEvent){
                TickEvent.PlayerTickEvent playerTickEvent= (TickEvent.PlayerTickEvent) event;
                if (playerTickEvent.phase== TickEvent.Phase.END && player.tickCount%100==0){
                    resurgence(player);
                }

            }
            else if (event instanceof LivingDamageEvent){
                starGazer(player, (LivingDamageEvent) event);
            }
        }

    }


    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,"Star Gazer",boldColor);
        ToolTipBuilder.addFullSetName(event,"Resurgence",boldColor);
        ToolTipBuilder.addPieceBonusName(event,"Unyielding Flame",boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,"Star Gazer",boldColor,"Applies glowing to attackers and reduces incoming damage for each nearby glowing entity.",infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,"Resurgence",boldColor,"Every 5 seconds, recover HP scaling with your current HP.",infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,"Unyielding Flame",boldColor,"Upon killing an enemy, ignite nearby enemies and gain absorption.",infoColor);
    }

    private void pieceEffect(Player player,int cnt){//unyieldingFlame
        int dura=3+cnt;
        int lvl= (int) (Math.ceil(cnt/2f));
        MobEffectInstance absorption=new MobEffectInstance(MobEffects.ABSORPTION,dura,lvl-1);
        player.addEffect(absorption);
        AABB aabb=player.getBoundingBox();
        aabb=aabb.inflate(4f);
        List<LivingEntity> entities=player.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate(player));
        if (!entities.isEmpty()){
            int size=entities.size();
            Random rand=new Random();
            
            int r=rand.nextInt(size);
            LivingEntity e=entities.remove(r);
            e.setSecondsOnFire(3+lvl);

            size--;

            r=rand.nextInt(size);
            e=entities.remove(r);
            e.setSecondsOnFire(3+lvl);
        }
    }

    private void resurgence(Player player){
        float hpPercent=player.getHealth()/player.getMaxHealth();
        float healAmt=hpPercent*4.5f;
        player.heal(healAmt);
    }

    private void starGazer(Player player,LivingDamageEvent event){
        if (event.getSource().getDirectEntity() instanceof LivingEntity){
            LivingEntity attacker= (LivingEntity) event.getSource().getDirectEntity();
            attacker.addEffect(new MobEffectInstance(MobEffects.GLOWING,5,1));
        }
        float dmgMult=1f;
        Predicate<LivingEntity> getGlowing= livingEntity -> livingEntity.hasEffect(MobEffects.GLOWING);
        AABB bounds=new AABB(player.position().subtract(new Vec3(6,2,6)),player.position().add(new Vec3(6,2,6)));
        TargetingConditions conds=TargetingConditions.forCombat();
        conds.selector(getGlowing);
        conds.range(6);
        int glowingEnemies=player.level().getNearbyEntities(LivingEntity.class, conds,player,bounds).size();
        dmgMult-=glowingEnemies*0.04f;
        if (dmgMult<0.5f){
            dmgMult=0.5f;
        }
        event.setAmount(event.getAmount()*dmgMult);
    }
}
