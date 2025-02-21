package com.github.celestial_awakening.events.armor_events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

public class ScorchedSuit extends ArmorEffect {
    String coreBreakSpeedID;
    String coreArmorPointID;
    String coreArmorToughnessID;

    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {

    }

    private void melt(Player player,int cnt,Event event){//break or attack event
        if (event instanceof PlayerEvent.BreakSpeed){
            PlayerEvent.BreakSpeed breakSpeedEvent= (PlayerEvent.BreakSpeed) event;
            BlockState blockState=breakSpeedEvent.getState();
            //my best bet it to use the tierregistry. see if the next lowest tier can harvest this block. if so, then boost
            Item item=player.getInventory().getSelected().getItem();

            if (item instanceof TieredItem){
                TieredItem tieredItem= (TieredItem) item;
                Tier lowerTier=TierSortingRegistry.getTiersLowerThan(tieredItem.getTier()).get(0);
                if (lowerTier!=null){
                    if (TierSortingRegistry.isCorrectTierForDrops(lowerTier,blockState)){
                        float bonusSpd=0.1f+0.15f*cnt;
                        breakSpeedEvent.setNewSpeed(breakSpeedEvent.getNewSpeed()+bonusSpd);
                    }
                }
            }
            //int i = net.minecraftforge.common.ForgeHooks.isCorrectToolForDrops(breakSpeedEvent.getState(),player) ? 30 : 100;
        }
        else if (event instanceof LivingDamageEvent){
            LivingDamageEvent livingDamageEvent= (LivingDamageEvent) event;
            LivingEntity entity=((LivingDamageEvent) event).getEntity();
            if (entity.isOnFire()){
                float bonusDmg=0.25f + 0.2f*cnt;
                livingDamageEvent.setAmount(livingDamageEvent.getAmount()+bonusDmg);
            }

        }
    }

    private void burningVeil(){//living hurt

    }

    private void coreResonance(Player player){//
        Level level=player.level();
        if (level.getDayTime()%100==0){//updates every 5 seconds
            double y=player.getY();
            double lowestY=level.getMinBuildHeight();
            double heightFromBottom=y-lowestY;
            //AttributeModifier breakSpeedMod=new AttributeModifier(UUID.fromString(coreBreakSpeedID),"Core Resonance-Mining Speed",1f, AttributeModifier.Operation.MULTIPLY_BASE);
            AttributeModifier armorPointMod=new AttributeModifier(UUID.fromString(coreArmorPointID),"Core Resonance-Armor",0, AttributeModifier.Operation.ADDITION);
            AttributeModifier armorToughnessMod=new AttributeModifier(UUID.fromString(coreArmorToughnessID),"Core Resonance-Toughness",0f, AttributeModifier.Operation.ADDITION);
            //player.getAttribute(Attributes.SPE).addTransientModifier(breakSpeedMod);
            if (player.getAttribute(Attributes.ARMOR).getModifier(UUID.fromString(coreArmorPointID))!=null){
                player.getAttribute(Attributes.ARMOR).getModifier(UUID.fromString(coreArmorPointID));
            }
            if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).getModifier(UUID.fromString(coreArmorToughnessID))!=null){

            }
            player.getAttribute(Attributes.ARMOR).addTransientModifier(armorPointMod);
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(armorToughnessMod);
        }


    }
}
