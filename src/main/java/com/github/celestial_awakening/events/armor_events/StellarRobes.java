package com.github.celestial_awakening.events.armor_events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

public class StellarRobes extends ArmorEffect {
    @Override
    public void performActions(Player player, int cnt, Event event) {
    }

    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {

    }
    private void nova(Player player, LivingDeathEvent event){
        if (event.getSource().getDirectEntity()==player){
            AABB bounds=new AABB(player.position().subtract(new Vec3(4,4,4)),player.position().add(new Vec3(4,4,4)));
            TargetingConditions conds=TargetingConditions.forCombat();
            conds.range(4);
            //DamageSources
            DamageSource novaBomb=player.level().damageSources().onFire();//maybe change to explosion
            //create particles

            //damage entity
            player.level().getNearbyEntities(LivingEntity.class,conds,player,bounds).forEach(entity->entity.hurt(novaBomb,6));

        }


    }


    private void neutronStar(){

    }


    private void conflagration(){

    }
}
