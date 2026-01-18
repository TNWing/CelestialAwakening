package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.entity.projectile.OrbiterProjectile;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

//moonstone armor set
public class LunarArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    Random random=new Random();

    String moonlightPath="4c80d010-d025-40de-80ad-6b262763cc30";

    //resonance isn't showing?

    public static final String RESONANCE_NAME = "tooltip.celestial_awakening.lunar_armor.resonance_name";
    public static final String RESONANCE_DESC = "tooltip.celestial_awakening.lunar_armor.resonance_desc";
    public static final String ORBITER_NAME = "tooltip.celestial_awakening.lunar_armor.orbiter_name";
    public static final String ORBITER_DESC = "tooltip.celestial_awakening.lunar_armor.orbiter_desc";
    public static final String BLESSED_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_name";
    public static final String BLESSED_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_desc";
    public static final String NO_BLESSING_DESC = "tooltip.celestial_awakening.lunar_armor.no_blessed_desc";
    public static final String PATH_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_path_name";
    public static final String PATH_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_path_desc";
    public static final String DEAL_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_deal_name";
    public static final String DEAL_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_deal_desc";
    public static final String FISH_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_fish_name";
    public static final String FISH_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_fish_desc";

    @Override
    public void onBlockBreak(BlockEvent.BreakEvent event,Player player,int cnt){
        pieceEffectBlockBreak(event,cnt);
    }
    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event,Player player,int cnt){
        if (cnt==4){
            if (player.tickCount%100==0){
                moonlitPath(event,player);
            }
        }
    }
    @Override
    public void onEquipmentChange(LivingEquipmentChangeEvent event,Player player,int cnt){
        if (cnt==4){
            moonlitPath(event,player);
        }
        else{
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID.fromString(moonlightPath));
        }
    }

    @Override
    public void onLivingDamageSelf(LivingDamageEvent event,Player player,int cnt){
        if (cnt==4){
            orbiter(event,player);
        }
    }

    public void onFishHookCreationEvent(ServerLevel serverLevel, FishingHook hook){
        if (serverLevel.getDayTime()>=nightStart && serverLevel.getMoonPhase()==4){
            int luck= ObfuscationReflectionHelper.getPrivateValue(FishingHook.class,hook,"f_37096_");
            int spd=ObfuscationReflectionHelper.getPrivateValue(FishingHook.class,hook,"f_37097_");
            ObfuscationReflectionHelper.setPrivateValue(FishingHook.class,hook,luck+2,"f_37096_");
            ObfuscationReflectionHelper.setPrivateValue(FishingHook.class, hook,spd+1,"f_37097_");
        }

    }
    public void onTrade(ServerLevel serverLevel,TradeWithVillagerEvent event,Player player){
        ItemStack costA=event.getMerchantOffer().getBaseCostA();
        ItemStack costB=event.getMerchantOffer().getCostB();
        if (serverLevel.getDayTime()>=nightStart && (serverLevel.getMoonPhase()==2) || serverLevel.getMoonPhase()==6){//if shady deal is active for player, refund part of costs
            if (costA.getCount()>1){
                ItemStack refundA=costA.copy();
                refundA.setCount((int) Math.ceil(costA.getCount()*0.1f));
                if (!player.addItem(refundA)){
                    ItemEntity itementity = player.drop(refundA, false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setTarget(player.getUUID());
                    }
                }

            }
            if (costB.getCount()>1){
                ItemStack refundB=costB.copy();
                refundB.setCount((int)(Math.ceil(costB.getCount()*0.1f)));
                if (!player.addItem(refundB)){
                    ItemEntity itementity = player.drop(refundB, false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setTarget(player.getUUID());
                    }
                }

            }
        }
    }

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
        Player player = event.getEntity();
        Level level = player.level();
        int time = (int) level.dayTime();//ranges from 0-24k
        ToolTipBuilder.addShiftInfo(event);
        if (time < nightStart) {//daytime
            ToolTipBuilder.addFullSetName(event,BLESSED_NAME,boldColor);
        } else {
            switch (level.getMoonPhase()) {
                case 0: {//full
                    ToolTipBuilder.addFullSetName(event,PATH_NAME,boldColor);
                    break;
                }
                case 2://half
                case 6: {
                    ToolTipBuilder.addFullSetName(event,DEAL_NAME,boldColor);
                    break;
                }
                case 4: {//new
                    ToolTipBuilder.addFullSetName(event,FISH_NAME,boldColor);
                    break;
                }
                default:{
                    ToolTipBuilder.addFullSetName(event,BLESSED_NAME,boldColor);
                }
            }
        }
        ToolTipBuilder.addFullSetName(event,ORBITER_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,RESONANCE_NAME,boldColor);
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
        Player player = event.getEntity();
        Level level = player.level();
        int time = (int) level.dayTime();//ranges from 0-24k
        if (time < nightStart) {//daytime
            ToolTipBuilder.addFullArmorSetComponent(event,BLESSED_NAME,boldColor,BLESSED_DESC,infoColor);
        } else {
            switch (level.getMoonPhase()) {
                case 0: {
                    ToolTipBuilder.addFullArmorSetComponent(event,PATH_NAME,boldColor,PATH_DESC,infoColor);
                    break;
                }
                case 2:
                case 6: {
                    ToolTipBuilder.addFullArmorSetComponent(event,DEAL_NAME,boldColor,DEAL_DESC,infoColor);
                    break;
                }
                case 4: {
                    ToolTipBuilder.addFullArmorSetComponent(event,FISH_NAME,boldColor,FISH_DESC,infoColor);
                    break;
                }
                default:{
                    ToolTipBuilder.addFullArmorSetComponent(event,BLESSED_NAME,boldColor,NO_BLESSING_DESC,infoColor);
                }
            }
        }
        ToolTipBuilder.addFullArmorSetComponent(event,ORBITER_NAME,boldColor,ORBITER_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,RESONANCE_NAME,boldColor,RESONANCE_DESC,infoColor);
        event.getToolTip().addAll(savedToolTip);
    }
    void pieceEffectBlockBreak(BlockEvent.BreakEvent event,int cnt){
        BlockState blockState=event.getState();
        if (blockState.is(BlockTags.DIRT) ){
            if (random.nextFloat(100)<cnt*0.4f){
                Player player=event.getPlayer();
                Level level=player.level();
                BlockPos blockPos=player.blockPosition();
                ItemEntity itemEntity;
                int roll=random.nextInt(0,10);
                if (roll<3){
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.MOONSTONE.get()));
                }
                else if (roll<6){
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(Items.BROWN_MUSHROOM));
                }
                else{
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(Items.RED_MUSHROOM));
                }
                level.addFreshEntity(itemEntity);
            }
        }
    }

    private void orbiter( LivingDamageEvent event,Player player){
        if (event.getEntity() == player && event.getSource().getEntity() instanceof LivingEntity){
            event.setAmount(event.getAmount()*0.95f);
            OrbiterProjectile orbiterProjectile=OrbiterProjectile.create(player.level(),player,140);
            player.level().addFreshEntity(orbiterProjectile);
        }
    }

    void moonlitPath(Event event, Player player){
        Level level=player.level();
        if (level.isDay()){//daytime
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID.fromString(moonlightPath));
        }
        else{
            int phase=level.getMoonPhase();
            if ((event instanceof LivingEquipmentChangeEvent || event instanceof TickEvent.PlayerTickEvent) && phase==0){//full
                //Moonlight Path: Drastically increased move speed
                AttributeModifier attributeModifier=new AttributeModifier(UUID.fromString(moonlightPath),"Moonlight Path Buff",0.75f,AttributeModifier.Operation.MULTIPLY_BASE);
                //works, but need to ensure that removal works

                if (player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID.fromString(moonlightPath))==null){
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(attributeModifier);
                }
            }
            else{
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID.fromString("4c80d010-d025-40de-80ad-6b262763cc30"));
            }
        }
    }

}
