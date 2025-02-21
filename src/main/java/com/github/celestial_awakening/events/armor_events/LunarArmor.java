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
        System.out.println("CHECKING FISHY");
        if (rand.nextInt(0,100)<cnt*12){
            System.out.println("ROLLED!");
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
        else{
            System.out.println("FAILED ROLL, chance to succeed was " + cnt*12);
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
                ToolTipBuilder.addFullSetName(event,"Blessed Night",boldColor);
            } else {
                switch (level.getMoonPhase()) {
                    case 0: {
                        ToolTipBuilder.addFullSetName(event,"Moonlight Path",boldColor);
                        break;
                    }
                    case 3:
                    case 7: {
                        ToolTipBuilder.addFullSetName(event,"Shady Deal",boldColor);
                        break;
                    }
                    case 5: {
                        ToolTipBuilder.addFullSetName(event,"Lucky Moon",boldColor);
                        break;
                    }
                }
            }
        }
        ToolTipBuilder.addFullSetName(event,"Orbiter",boldColor);
        ToolTipBuilder.addPieceBonusName(event,"Lunar Resonance",boldColor);

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        Player player = event.getEntity();
        Level level = player.level();
        int time = (int) level.dayTime();//ranges from 0-24k
        if (time % 120 != 0) {//update the desc every 6 sec
            if (time < nightStart) {//daytime
                ToolTipBuilder.addFullArmorSetComponent(event,"Blessed Night",boldColor,"Receive a buff at night depending on the moon phase.",infoColor);
            } else {
                switch (level.getMoonPhase()) {
                    case 0: {
                        ToolTipBuilder.addFullArmorSetComponent(event,"Moonlight Path",boldColor,"Drastically increased movement speed.",infoColor);
                        break;
                    }
                    case 3:
                    case 7: {
                        ToolTipBuilder.addFullArmorSetComponent(event,"Shady Deal",boldColor,"Refund a portion of materials used in villager trades.",infoColor);
                        break;
                    }
                    case 5: {
                        ToolTipBuilder.addFullArmorSetComponent(event,"Lucky Moon",boldColor,"Increases chance of fishing up treasures.",infoColor);
                        break;
                    }
                }
            }
        }
        ToolTipBuilder.addFullArmorSetComponent(event,"Orbiter",boldColor,"When attacked, creates a comet that follows the wearer. After a short delay, the comet will launch towards the nearest enemy. If no enemy present, the comet is consumed to heal the user slightly.",infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,"Lunar Resonance",boldColor,"Chance to gain special items upon breaking blocks or fishing up items.",infoColor);
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
