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

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class EverlightArmor extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;

    public static final String UNYIELDING_NAME = "tooltip.celestial_awakening.everlight_armor.unyielding_name";
    public static final String UNYIELDING_DESC = "tooltip.celestial_awakening.everlight_armor.unyielding_desc";
    public static final String STAR_GAZER_NAME = "tooltip.celestial_awakening.everlight_armor.star_gazer_name";
    public static final String STAR_GAZER_DESC = "tooltip.celestial_awakening.everlight_armor.star_gazer_desc";
    public static final String RESURGENCE_NAME = "tooltip.celestial_awakening.everlight_armor.resurgence_name";
    public static final String RESURGENCE_DESC = "tooltip.celestial_awakening.everlight_armor.resurgence_desc";
    MobEffectInstance glow=new MobEffectInstance(MobEffects.GLOWING,200,1);
    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event,Player player, int cnt){
        if (cnt==4){
            if (event.phase== TickEvent.Phase.END && player.tickCount%100==0){
                resurgence(player);
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
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,STAR_GAZER_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,RESURGENCE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,UNYIELDING_NAME,boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,STAR_GAZER_NAME,boldColor,STAR_GAZER_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,RESURGENCE_NAME,boldColor,RESURGENCE_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,UNYIELDING_NAME,boldColor,UNYIELDING_DESC,infoColor);
    }

    private void unyieldingFlame(LivingDeathEvent event,Player player,int cnt){
        int dura=3+cnt;
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

    private void starGazer(LivingDamageEvent event,Player player){
        if (event.getSource().getEntity() instanceof LivingEntity){
            LivingEntity attacker= (LivingEntity) event.getSource().getEntity();
            if (attacker.canBeAffected(glow)){
                attacker.addEffect(glow);
            }
            else{
                System.out.println("attacker immune to glow");
                player.heal(0.1f);
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
