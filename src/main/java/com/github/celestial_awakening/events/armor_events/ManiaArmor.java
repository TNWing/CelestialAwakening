package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

public class ManiaArmor extends ArmorEffect {

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
        @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.livingEntityCapability);
        capOptional.ifPresent(cap->{

        });
    }

    private void hysteria(Player player, LivingDamageEvent event){
        if (event.getEntity()==player){
            @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.livingEntityCapability);
            capOptional.ifPresent(cap->{

            });
        }
    }
}
