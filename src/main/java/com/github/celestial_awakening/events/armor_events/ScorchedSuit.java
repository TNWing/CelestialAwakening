package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScorchedSuit extends ArmorEffect{
    int boldColor=0x756252;
    int infoColor=0x3D3229;
    public static final String RESONANCE_NAME = "tooltip.celestial_awakening.scorched_suit.core_resonance_name";
    public static final String RESONANCE_DESC = "tooltip.celestial_awakening.scorched_suit.core_resonance_desc";
    public static final String MELT_NAME = "tooltip.celestial_awakening.scorched_suit.melt_name";
    public static final String MELT_DESC = "tooltip.celestial_awakening.scorched_suit.melt_desc";
    UUID coreResonance= UUID.fromString("af89b4ac-8954-473b-a0e1-a503e0c6e2d0");
    float[] meltDmg={0.2f,0.4f,0.7f,1f};
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
        ToolTipBuilder.addFullSetName(event,RESONANCE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,MELT_NAME,boldColor);
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
        ToolTipBuilder.addFullArmorSetComponent(event,RESONANCE_NAME,boldColor,RESONANCE_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,MELT_NAME,boldColor,MELT_DESC,infoColor);
        event.getToolTip().addAll(savedToolTip);
    }
    /*
    Full Set Bonus: Core Resonance
Increases armor points, toughness, and mining speed the lower the y coordinate the wearer is at.
Piece Bonus: Melt
Gain bonus damage against enemies on fire.
Gain bonus mining speed when mining blocks whose required breaking level is lower than your tool

     */
    @Override
    public void onEquipmentChange(LivingEquipmentChangeEvent event, Player player, int cnt){
        if (cnt!=4){
            player.getAttribute(Attributes.ARMOR).removeModifier(coreResonance);
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(coreResonance);
        }
    }

    @Override
    public void onLivingDamageOthers(LivingDamageEvent event,Player player, int cnt){
        melt(event,cnt);
    }

    public void melt(LivingDamageEvent event, int cnt){
        LivingEntity entity=event.getEntity();
        if (entity.isOnFire()){
            event.setAmount(event.getAmount()+meltDmg[cnt-1]);
        }
    }


    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event, Player player, int cnt){
        if (cnt==4){
            double y=player.getY();
            if (player.tickCount%50==0 && y<0){

                double diff=0-y;
                AttributeModifier armorMod=new AttributeModifier(coreResonance,"Core Resonance",diff/25d,AttributeModifier.Operation.ADDITION);
                AttributeModifier toughMod=new AttributeModifier(coreResonance,"Core Resonance",diff/50d,AttributeModifier.Operation.ADDITION);
                //System.out.printf("OUR MODS ARE %s   %s",armorMod,toughMod);
                player.getAttribute(Attributes.ARMOR).addTransientModifier(armorMod);
                player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(toughMod);

            }
            else{
                player.getAttribute(Attributes.ARMOR).removeModifier(coreResonance);
                player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(coreResonance);
            }
        }

    }


}
