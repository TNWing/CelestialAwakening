package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

public class KnightmareSuit extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    /*
    Sword & Shield
        Gain 2 defensive points on successful shield blocks.
        Gain 2 offensive points on successful full charged attacks.
        Max of 6 points each
        Getting hit will consume 1 offensive point
        Dealing damage will consume 1 defensive point to increase

        V2
            Landing a hit will reduce the damage taken from the next attack. Blocking an attack will increase the damage of your next attack.
    Honor
        Hitting an enemy multiple times applies Honor Duel to the user
        Decreases damage taken from other enemies.
        Decreases damage dealt to other enemies.
        Honor Duel is removed if the user or the target is killed.
    Piece Bonus: Infamy
        Killing an enemy grants notoriety for some time.
        Notoriety strength scales with the slain enemy’s max HP.
        Notoriety increases damage dealt by X per 10 HP.
        Notoriety is replaced if a stronger enemy is killed

     */
    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (event instanceof ItemTooltipEvent){
            onItemTooltipEvent((ItemTooltipEvent) event,cnt);
        }
        else if (event instanceof LivingDeathEvent){
            infamy(player, (LivingDeathEvent) event,cnt);
        }
        else if (event instanceof LivingHurtEvent){
            infamyBoost((LivingHurtEvent) event,player);
        }
        else if (cnt>=4){

        }
    }

    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,"Sword & Shield",boldColor);
        ToolTipBuilder.addFullSetName(event,"Honor Duel",boldColor);
        ToolTipBuilder.addPieceBonusName(event,"Infamy",boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,"Sword & Shield",boldColor,"TBD",infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,"Honor Duel",boldColor,"TBD",infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,"Infamy",boldColor,
                String.format("Upon killing an enemy, gain %s base damage for every 10 HP the enemy had.",cnt*0.25f),
                infoColor);
    }

    private void infamy(Player player, LivingDeathEvent event,int cnt){
        if(event.getSource().getDirectEntity()==player){
            LivingEntityCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                Integer[] data=cap.getAbilityData("Knightmare_Infamy");
                float mHp=event.getEntity().getMaxHealth();
                int n= (int) ((mHp/10) * cnt*0.25f);
                if (data!=null){
                    int currentVal=data[0];
                    if (currentVal<=n){
                        cap.insertIntoAbilityMap("Knightmare_Infamy",15*20,new Integer[]{n});
                    }
                }
                else{
                    cap.insertIntoAbilityMap("Knightmare_Infamy",15*20,new Integer[]{n});
                }
            }
        }
    }
    private void infamyBoost(LivingHurtEvent event,Player player){
        if(event.getSource().getDirectEntity()==player){
            LivingEntityCapability cap=player.getCapability(PlayerCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                Integer[] data=cap.getAbilityData("Knightmare_Infamy");
                if (data!=null){
                    int currentVal=data[0];
                    event.setAmount(event.getAmount()+currentVal);
                }
            }
        }
    }
    private void applyHonorDuel(Player player, LivingHurtEvent event){

    }

    private void honorDmgReduction(Player player){

    }
}
