package com.github.celestial_awakening.events.armor_events;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;

public abstract class ArmorEffect {
    static int nightStart=12000;
    static int nightEnd=24000;

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

    public void onEquipmentChange(LivingEquipmentChangeEvent event, Player player, int cnt){

    }



    public void onPlayerTick(TickEvent.PlayerTickEvent event, Player player, int cnt){

    }


    public void onLivingDeath(LivingDeathEvent event,Player player,int cnt){

    }

    public void onLivingHurtOthers(LivingHurtEvent event,Player player,int cnt){

    }

    public void onLivingHurtSelf(LivingHurtEvent event,Player player,int cnt){

    }

    public void onLivingDamageOthers(LivingDamageEvent event,Player player, int cnt){

    }

    public void onLivingDamageSelf(LivingDamageEvent event,Player player, int cnt){

    }

    public void onBlockBreak(BlockEvent.BreakEvent event,Player player,int cnt){

    }
    public void forceUpdate(Player player,int cnt){//used to force updates such as due to player death

    }

}
