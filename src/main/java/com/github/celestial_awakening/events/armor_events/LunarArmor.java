package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.entity.projectile.OrbiterProjectile;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.Random;
import java.util.UUID;

//moonstone armor set
public class LunarArmor extends ArmorEffect {
    int boldColor=0xC0c0c0;
    int infoColor=0xC3c3c3;
    Random random=new Random();

    String moonlightPath="4c80d010-d025-40de-80ad-6b262763cc30";

    public static final String RESONANCE_NAME = "tooltip.celestial_awakening.lunar_armor.resonance_name";
    public static final String RESONANCE_DESC = "tooltip.celestial_awakening.lunar_armor.resonance_desc";
    public static final String ORBITER_NAME = "tooltip.celestial_awakening.lunar_armor.orbiter_name";
    public static final String ORBITER_DESC = "tooltip.celestial_awakening.lunar_armor.orbiter_desc";
    public static final String BLESSED_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_name";
    public static final String BLESSED_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_desc";
    public static final String PATH_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_path_name";
    public static final String PATH_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_path_desc";
    public static final String DEAL_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_deal_name";
    public static final String DEAL_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_deal_desc";
    public static final String FISH_NAME = "tooltip.celestial_awakening.lunar_armor.blessed_fish_name";
    public static final String FISH_DESC = "tooltip.celestial_awakening.lunar_armor.blessed_fish_desc";
/*  TODO:
    piece effect should trigger on
    -trade
    -fish
    -block break


    also, should use loottables instead of modifying these events
 */
    @Override
    public void onBlockBreak(BlockEvent.BreakEvent event,Player player,int cnt){
        pieceEffectBlockBreak(event,cnt);
    }
    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event,Player player,int cnt){
        if (cnt==4){
            if (event.phase== TickEvent.Phase.END && player.tickCount%100==0){
                blessedNightEffects(event,player);
            }
        }
    }
    @Override
    public void onEquipmentChange(LivingEquipmentChangeEvent event,Player player,int cnt){
        if (cnt==4){
            blessedNightEffects(event,player);
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

    public void onFishEvent(ItemFishedEvent event,int cnt){
        Random rand=new Random();
        if (rand.nextInt(0,100)<cnt*12){
            Player player=event.getEntity();
            int roll=rand.nextInt(0,10);
            ItemEntity itemEntity;
            if (roll<5){
                 itemEntity =new ItemEntity(player.level(),player.getX(),player.getY(),player.getZ(),new ItemStack(ItemInit.MOONSTONE.get()));
            }
            else{
                itemEntity =new ItemEntity(player.level(),player.getX(),player.getY(),player.getZ(),new ItemStack(ItemInit.LUNAR_SCALE.get()));
            }
            player.level().addFreshEntity(itemEntity);
        }
    }

    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        Player player = event.getEntity();
        Level level = player.level();
        int time = (int) level.dayTime();//ranges from 0-24k
        ToolTipBuilder.addShiftInfo(event);
        if (time % 120 != 0) {//update the desc every 6 sec
            if (time < nightStart) {//daytime
                ToolTipBuilder.addFullSetName(event,BLESSED_NAME,boldColor);
            } else {
                switch (level.getMoonPhase()) {
                    case 0: {
                        ToolTipBuilder.addFullSetName(event,PATH_NAME,boldColor);
                        break;
                    }
                    case 3:
                    case 7: {
                        ToolTipBuilder.addFullSetName(event,DEAL_NAME,boldColor);
                        break;
                    }
                    case 5: {
                        ToolTipBuilder.addFullSetName(event,FISH_NAME,boldColor);
                        break;
                    }
                }
            }
        }
        ToolTipBuilder.addFullSetName(event,ORBITER_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,RESONANCE_NAME,boldColor);

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        Player player = event.getEntity();
        Level level = player.level();
        int time = (int) level.dayTime();//ranges from 0-24k
        if (time % 120 != 0) {//update the desc every 6 sec
            if (time < nightStart) {//daytime
                ToolTipBuilder.addFullArmorSetComponent(event,BLESSED_NAME,boldColor,BLESSED_DESC,infoColor);
            } else {
                switch (level.getMoonPhase()) {
                    case 0: {
                        ToolTipBuilder.addFullArmorSetComponent(event,PATH_NAME,boldColor,PATH_DESC,infoColor);
                        break;
                    }
                    case 3:
                    case 7: {
                        ToolTipBuilder.addFullArmorSetComponent(event,DEAL_NAME,boldColor,DEAL_DESC,infoColor);
                        break;
                    }
                    case 5: {
                        ToolTipBuilder.addFullArmorSetComponent(event,FISH_NAME,boldColor,FISH_DESC,infoColor);
                        break;
                    }
                }
            }
        }
        ToolTipBuilder.addFullArmorSetComponent(event,ORBITER_NAME,boldColor,ORBITER_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,RESONANCE_NAME,boldColor,RESONANCE_DESC,infoColor);
    }
    void pieceEffectBlockBreak(BlockEvent.BreakEvent event,int cnt){
        BlockState blockState=event.getState();
        Block block=blockState.getBlock();
        if (blockState.is(BlockTags.DIRT) ){
            if (random.nextFloat(100)<cnt*0.4f){
                Player player=event.getPlayer();
                Level level=player.level();
                BlockPos blockPos=player.blockPosition();
                ItemEntity itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.MOONSTONE.get()));
                level.addFreshEntity(itemEntity);
            }
        }
    }


    void setEffect(Player player,int cnt,Event event){
        if (event instanceof TickEvent.PlayerTickEvent){
        }
        else if (cnt==4){
            blessedNightEffects(event,player);
        }
    }

    private void orbiter( LivingDamageEvent event,Player player){//still hits
        if (event.getEntity() == player && event.getSource().getDirectEntity() instanceof LivingEntity){
            LivingEntity livingEntity= (LivingEntity) event.getSource().getDirectEntity();
            event.setAmount(event.getAmount()*0.95f);
            OrbiterProjectile orbiterProjectile=OrbiterProjectile.create(player.level(),player,140);
            player.level().addFreshEntity(orbiterProjectile);
        }
    }



    void blessedNightEffects(Event event,Player player){
        Level level=player.level();
        int time=(int)level.dayTime();//ranges from 0-24k
        if (time<nightStart){//daytime
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID.fromString(moonlightPath));
        }
        else{
            int phase=level.getMoonPhase();

            if ((event instanceof LivingEquipmentChangeEvent || event instanceof TickEvent.LevelTickEvent) && phase==0){//full
                //Moonlight Path: Drastically increased move speed
                AttributeModifier attributeModifier=new AttributeModifier(UUID.fromString(moonlightPath),"Moonlight Path Buff",0.75f,AttributeModifier.Operation.MULTIPLY_BASE);
                //works, but need to ensure that removal works

                if (player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID.fromString(moonlightPath))==null){
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(attributeModifier);
                }
                //need to remove modifier when
                        /*
                        no longer has full set:
                        daytime:done
                        different moon phase:done

                         */
            }
            else{
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID.fromString("4c80d010-d025-40de-80ad-6b262763cc30"));
                if((phase==3 || phase == 7) && event instanceof TradeWithVillagerEvent){//half
                    //Shady Deal: Chance to improve villager trade outputs
                }
                //bountiful is handled via loot modifier, crescent: 4,6
                //must also handle new moon effect via fishing loot mod, new moon:5
            }
        }
    }

}
