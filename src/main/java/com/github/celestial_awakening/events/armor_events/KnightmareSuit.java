package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class KnightmareSuit extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    public static String honorDuel="Knightmare_Honor_Duel";
    public static String infamy="Knightmare_Infamy";
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
        Hitting an enemy applies Honor Duel to the user
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


    @Override
    public void onLivingDeath(LivingDeathEvent event,Player player,int cnt){
        infamy(event,player,cnt);
    }

    @Override
    public void onLivingHurtOthers(LivingHurtEvent event,Player player,int cnt){
        infamyBoost(event,player);
        if (cnt==4){
            applyHonorDuel(event,player);
        }
    }

    private void infamy(LivingDeathEvent event,Player player,int cnt){
        if(event.getSource().getEntity()==player){

            LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                Integer[] data=cap.getAbilityData(infamy);
                float mHp=event.getEntity().getMaxHealth();
                int n= (int) ((mHp/10) * cnt*0.25f);

                if (data!=null){
                    int currentVal=data[0];
                    if (currentVal<=n){
                        cap.insertIntoAbilityMap(infamy,15*20,new Integer[]{n});
                    }
                }
                else{
                    cap.insertIntoAbilityMap(infamy,15*20,new Integer[]{n});
                }
            }
        }
    }
    private void infamyBoost(LivingHurtEvent event,Player player){
        if(event.getSource().getEntity()==player){

            LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                Integer[] data=cap.getAbilityData(infamy);
                if (data!=null){
                    System.out.println("infamy boost");
                    int currentVal=data[0];
                    event.setAmount(event.getAmount()+currentVal);
                }
            }
        }
    }
    private void applyHonorDuel(LivingHurtEvent event,Player player){
        if(event.getSource().getEntity()==player){
            LivingEntity target=event.getEntity();
            LivingEntityCapability pCap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            LivingEntityCapability targetCap=target.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            if (pCap!=null && targetCap!=null){
                if (targetCap.getAbilityData(honorDuel)==null &&
                        pCap.getAbilityData(honorDuel)==null){//only apply if neither target is under honor duel
                    Integer[] dataForPlayer= {target.getId(),player.getId()};//format: corresponding entity uuid, applying entity uuid
                    Integer[] dataForTarget= {player.getId(),player.getId()};//format: corresponding entity uuid, applying entity uuid
                    pCap.insertIntoAbilityMap(honorDuel,-10,dataForPlayer);
                    targetCap.insertIntoAbilityMap(honorDuel,-10,dataForTarget);
                }

            }
        }
    }
    //maybe make an honor duel effect thats invisible and essentially does nothing
    //and whhen an entity dies, check if they had honor duel. if so, grab cap data
    //that might not work particularly well, maybe i should put honor duel into the living entity cap
    //or just remerge player and living caps
    //i can also handle this in event manager, since it triggers upon the entity dying by any means
    //the effect should be removed if too far away from target, and this should be checked every 5 seconds.
    private void removeHonorDuel(Player player, LivingDeathEvent event){
        if(event.getSource().getEntity()==player){
            LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                Integer[] data=cap.getAbilityData("Knightmare_HonorDuel");
            }
        }
    }

    private void honorDmgReduction(Player player, LivingDamageEvent event){
        if (event.getEntity() == player){
            LivingEntityCapability pCap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
        }

    }
}
