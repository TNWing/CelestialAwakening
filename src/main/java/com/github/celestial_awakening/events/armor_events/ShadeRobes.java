package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.events.DelayedFunctionManager;
import com.github.celestial_awakening.events.command_patterns.GenericCommandPattern;
import com.github.celestial_awakening.util.CA_UUIDs;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;

public class ShadeRobes extends ArmorEffect {

        /*
            @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()){
            //It seems that, if player dies, their capability doesn't work anymore. See why
            //it seems to work now? maybe it was the onLivingEntityDeath thing i changed
            //should reset capability data so maybe change this
            event.getOriginal().getCapability(EntityEnchantmentDataProvider.EnchantmentData).ifPresent(
                    oldStore->event.getOriginal().getCapability(EntityEnchantmentDataProvider.EnchantmentData).ifPresent(newStore->newStore.copyDataFrom(oldStore)));
        }
    }
         */


    @Override
    public void onEquipmentChange(LivingEquipmentChangeEvent event,Player player,int cnt){
        pieceEffect(player,cnt);
    }
    @Override
    public void onLivingDamageSelf(LivingDamageEvent event,Player player,int cnt){
        if (cnt==4){
            fadeEffect(event,player);
        }
    }
    @Override
    public void onLivingHurtOthers(LivingHurtEvent event,Player player,int cnt){
        if (cnt==4){
            shadowStrikeEffect(event,player);
        }
    }

    /*
  "tooltip.celestial_awakening.shade_robes.range_name": "Expanding Darkness",
"tooltip.celestial_awakening.shade_robes.range_desc": "Increases attack range by %s",
"tooltip.celestial_awakening.shade_robes.fade_name": "Fade",
"tooltip.celestial_awakening.shade_robes.fade_desc":"Each instance of damage taken is delayed by 5 seconds.\nWhen an instance of damage is dealt, the amount is reduced based on how far the wearer is from when they were hit.\n",
"tooltip.celestial_awakening.shade_robes.strike_name": "Shadow Strike",
"tooltip.celestial_awakening.shade_robes.strike_desc":"Increases damage dealt based on distance from the target.",

 */
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    public static final String STRIKE_NAME = "tooltip.celestial_awakening.shade_robes.strike_name";
    public static final String STRIKE_DESC = "tooltip.celestial_awakening.shade_robes.strike_desc";
    public static final String FADE_NAME = "tooltip.celestial_awakening.shade_robes.fade_name";
    public static final String FADE_DESC = "tooltip.celestial_awakening.shade_robes.fade_desc";
    public static final String ED_NAME = "tooltip.celestial_awakening.shade_robes.range_name";
    public static final String ED_DESC = "tooltip.celestial_awakening.shade_robes.range_desc";
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
        ToolTipBuilder.addFullSetName(event, FADE_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event, STRIKE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event, ED_NAME,boldColor);
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
        ToolTipBuilder.addFullArmorSetComponent(event, FADE_NAME,boldColor, FADE_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event, STRIKE_NAME,boldColor, STRIKE_DESC,infoColor);
        float rangeIncrease= (float) (0.1f*Math.pow(2,cnt-1));
        ToolTipBuilder.addArmorPieceComponent(event, ED_NAME,boldColor, ED_DESC,new Object[]{rangeIncrease},infoColor);
        event.getToolTip().addAll(savedToolTip);
    }
    class FadeMitigationCommandPattern extends GenericCommandPattern {//might replace this and just put it into abilityMap
        public FadeMitigationCommandPattern(Object[] params) {
            super(params);
        }
        /*
        params consist of
        player entity to damage
        dmge amount
        location of the einitial damage instance
         */
        public boolean execute(){
            Player player= (Player) this.params[0];
            float dmg= (float) this.params[1];
            Vec3 prevPos= (Vec3) this.params[2];
            Vec3 currentPos=player.position();
            double dist=currentPos.distanceTo(prevPos);
            if (dist>5){
                dist=5;
            }
            //0 to 5 dist
            //0 to 54% dmg reduction
            float dmgMult= (100f-(float) (4.5f*Math.pow(dist,1.55D)))/100f;
            //System.out.println("WE ARE GOING FADE TO REDUCE FROM " + dmg + " TO " + dmg*dmgMult);
            player.setHealth(player.getHealth()-dmg*dmgMult);
            return false;
        }

    }

    void pieceEffect(Player player,int amt){
        if (amt==0 && player.getAttribute(ForgeMod.ENTITY_REACH.get()).getModifier(CA_UUIDs.shadePieceEffectID)!=null){
            player.getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(CA_UUIDs.shadePieceEffectID);
        }
        else{

            float rangeIncrease= (float) (0.1f*Math.pow(2,amt-1));
            if (amt==0){
                rangeIncrease=0;
            }
            AttributeModifier attributeModifier=new AttributeModifier(CA_UUIDs.shadePieceEffectID,"Expanding Darkness",rangeIncrease,AttributeModifier.Operation.ADDITION);
            if (player.getAttribute(ForgeMod.ENTITY_REACH.get()).getModifier(CA_UUIDs.shadePieceEffectID)!=null){
                player.getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(CA_UUIDs.shadePieceEffectID);
            }
            player.getAttribute(ForgeMod.ENTITY_REACH.get()).addTransientModifier(attributeModifier);
        }

    }
    void fadeEffect(LivingDamageEvent event,Player player){
        if (event.getEntity()==player){
            float dmg=event.getAmount();
            Vec3 dmgPos=player.position();
            FadeMitigationCommandPattern command=new FadeMitigationCommandPattern(new Object[]{player,dmg,dmgPos});
            DelayedFunctionManager.delayedFunctionManager.insertIntoPlayerMap(player,command,100);
            event.setCanceled(true);
        }
    }
    void shadowStrikeEffect(LivingHurtEvent event,Player player){
        if (event.getSource().getEntity()==player){
            Vec3 playerPos=player.position();
            Vec3 targetPos=event.getEntity().position();
            double dist=targetPos.distanceTo(playerPos);
            float mult=((int) (dist*10));
            event.setAmount(event.getAmount()+0.125f*mult);
        }
    }
}
