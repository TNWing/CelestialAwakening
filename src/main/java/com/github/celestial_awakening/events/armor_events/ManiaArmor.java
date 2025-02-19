package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Predicate;

public class ManiaArmor extends ArmorEffect {
    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (cnt==4){
            frenzy(player,event);
            if (event instanceof LivingDamageEvent){
                hysteria(player, (LivingDamageEvent) event);
            }

        }
    }

    @Override
    public void onItemTooltipEvent(ItemTooltipEvent event, int cnt) {

    }


    private void nightmare(){

    }
    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {

    }
    private void frenzy(Player player,Event event){
        PlayerCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null){

        }
    }

    private void hysteria(Player player, LivingDamageEvent event){
        if (event.getEntity()==player){
            PlayerCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                Predicate predicate = null;
                //MobEffectInstance effects[]=player.getActiveEffects().stream().filter(predicate).toArray();
            }
            //
        }
    }
}
