package com.github.celestial_awakening.events.armor_events;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

public abstract class ArmorEffect {
    static int nightStart=12000;
    static int nightEnd=24000;
    public abstract void performActions(Player player, int cnt, Event event);


    public void onItemTooltipEvent(ItemTooltipEvent event, int cnt) {
        if (cnt<1){
            cnt=1;
        }
        if (Screen.hasShiftDown()){
            longDesc(event,cnt);
        }
        else{
            effectNames(event, cnt);
        }
    }

    abstract void effectNames(ItemTooltipEvent event, int cnt);

    abstract void longDesc(ItemTooltipEvent event,int cnt);

    //abstract void onLivingHurt(LivingHurtEvent event,int cnt);
}
