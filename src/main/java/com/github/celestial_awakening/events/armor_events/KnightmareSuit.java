package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KnightmareSuit extends ArmorEffect{
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    public static String honorDuel="Knightmare_Honor_Duel";
    public static String infamy="Knightmare_Infamy";


    public static final String INFAMY_NAME = "tooltip.celestial_awakening.knightmare_suit.infamy_name";
    public static final String INFAMY_DESC = "tooltip.celestial_awakening.knightmare_suit.infamy_desc";
    public static final String HONOR_DUEL_NAME = "tooltip.celestial_awakening.knightmare_suit.honor_duel_name";
    public static final String HONOR_DUEL_DESC = "tooltip.celestial_awakening.knightmare_suit.honor_duel_desc";
    public static final String SS_NAME = "tooltip.celestial_awakening.knightmare_suit.ss_name";
    public static final String SS_DESC = "tooltip.celestial_awakening.knightmare_suit.ss_desc";
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
    //TODO: have honor duel removed upon entity death

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
        //ToolTipBuilder.addFullSetName(event, SS_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,HONOR_DUEL_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,INFAMY_NAME,boldColor);
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
        //ToolTipBuilder.addFullArmorSetComponent(event,SS_NAME,boldColor,SS_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,HONOR_DUEL_NAME,boldColor,HONOR_DUEL_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,INFAMY_NAME,boldColor,
               INFAMY_DESC,new Object[]{cnt*0.25f},
                infoColor);
        event.getToolTip().addAll(savedToolTip);
    }

    @Override
    public void onEquipmentChange(LivingEquipmentChangeEvent event, Player player, int cnt){

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

            @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.playerCapability);
            capOptional.ifPresent(cap->{
                Object[] data= cap.getAbilityData(infamy);
                float mHp=event.getEntity().getMaxHealth();
                int n= (int) ((mHp/10) * cnt*0.25f);

                if (data!=null){
                    int currentVal= (int) data[0];
                    if (currentVal<=n){
                        cap.insertIntoAbilityMap(infamy,15*20,new Integer[]{n});
                    }
                }
                else{
                    cap.insertIntoAbilityMap(infamy,15*20,new Integer[]{n});
                }
            });
        }
    }
    private void infamyBoost(LivingHurtEvent event,Player player){
        if(event.getSource().getEntity()==player){

            @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.playerCapability);
            capOptional.ifPresent(cap->{
                if (cap.hasAbility(infamy)){
                    Object[] data= cap.getAbilityData(infamy);
                    if (data!=null){
                        int currentVal= (int) data[0];
                        event.setAmount(event.getAmount()+currentVal);
                    }
                }
            });

        }
    }
    private void applyHonorDuel(LivingHurtEvent event,Player player){
        if(event.getSource().getEntity()==player){
            LivingEntity target=event.getEntity();
            @NotNull LazyOptional<LivingEntityCapability> pCapOptional=player.getCapability(LivingEntityCapabilityProvider.playerCapability);
            @NotNull LazyOptional<LivingEntityCapability> targetCapOptional=target.getCapability(LivingEntityCapabilityProvider.playerCapability);
            pCapOptional.ifPresent(pCap->{
                targetCapOptional.ifPresent(targetCap->{
                    if (targetCap.getAbilityData(honorDuel)==null &&
                            pCap.getAbilityData(honorDuel)==null){//only apply if neither target is under honor duel
                        UUID[] dataForPlayer= {target.getUUID(),player.getUUID()};//format: corresponding entity uuid, applying entity uuid
                        UUID[] dataForTarget= {player.getUUID(),player.getUUID()};//format: corresponding entity uuid, applying entity uuid
                        pCap.insertIntoAbilityMap(honorDuel,-10,dataForPlayer);
                        targetCap.insertIntoAbilityMap(honorDuel,-10,dataForTarget);
                        //HONOR DUEL FOR c77386e1-8d59-4429-af55-7f23135e7faf AND 380df991-f603-344c-a090-369bad2a924a
                    }
                });
            });
        }
    }
}
