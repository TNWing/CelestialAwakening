package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RadiantArmor extends ArmorEffect {
    Random random=new Random();
    int boldColor=0xe89a2c;
    int infoColor=0xdaa458;//daa458
    static Item[] seedArray={Items.BEETROOT_SEEDS,Items.WHEAT_SEEDS,Items.MELON_SEEDS,Items.PUMPKIN_SEEDS};

    public static final String ABS_NAME = "tooltip.celestial_awakening.radiant_armor.abs_name";
    public static final String ABS_DESC = "tooltip.celestial_awakening.radiant_armor.abs_desc";
    public static final String PARTICLE_NAME = "tooltip.celestial_awakening.radiant_armor.particle_name";
    public static final String PARTICLE_DESC = "tooltip.celestial_awakening.radiant_armor.particle_desc";
    public static final String WAVE_NAME = "tooltip.celestial_awakening.radiant_armor.wave_name";
    public static final String WAVE_DESC = "tooltip.celestial_awakening.radiant_armor.wave_desc";


    @Override
    public void onBlockBreak(BlockEvent.BreakEvent event,Player player,int cnt){
        pieceEffect_Crop(event,cnt);
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event,Player player,int cnt){
        if (cnt==4){
            excitedParticles(event);
            rejuvenatingWave(event);
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
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,PARTICLE_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,WAVE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,ABS_NAME,boldColor);
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
        ToolTipBuilder.addFullArmorSetComponent(event,PARTICLE_NAME,boldColor,PARTICLE_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,WAVE_NAME,boldColor,WAVE_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,ABS_NAME,boldColor,ABS_DESC,infoColor);
        event.getToolTip().addAll(savedToolTip);
    }
    public void pieceEffect_Crop(BlockEvent.BreakEvent event, int cnt){//not broken, but maybe too low, also reword the desc of the effect
        if (!event.getLevel().isClientSide() && event.getState().getBlock() instanceof CropBlock && !event.isCanceled()){
            if (random.nextInt(100)<cnt*3.75f){
                Player player=event.getPlayer();
                Level level=player.level();
                BlockPos blockPos=player.blockPosition();
                int rollNum=random.nextInt(0,11);
                ItemEntity itemEntity;
                if (rollNum<5){
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.SUNSTONE.get()));
                }
                else{
                    int seedIndex=random.nextInt(4);
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(seedArray[seedIndex]));
                }

                level.addFreshEntity(itemEntity);
            }

        }
    }

    int range=9;
    public void excitedParticles(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (player instanceof ServerPlayer){
            if (player.tickCount % Config.excitedParticlesCropTickInterval ==0){
                ServerLevel serverLevel= (ServerLevel) player.level();
                for (int x=-range;x<=range;x++){
                    for (int z=-range;z<=range;z++){
                        for (int y=-range;y<=range;y++){
                            BlockPos blockPos=player.blockPosition();
                            blockPos=blockPos.offset(x,y,z);
                            BlockState blockState=serverLevel.getBlockState(blockPos);
                            Block block=blockState.getBlock();
                            if (block instanceof CropBlock||
                                    block instanceof SaplingBlock){
                                blockState.randomTick(serverLevel,blockPos,serverLevel.random);
                            }
                        }
                    }
                }
            }
            if (player.tickCount % Config.excitedParticlesAnimalTickInterval ==0){
                ServerLevel serverLevel= (ServerLevel) player.level();
                AABB aabb=new AABB(player.getX()-range,player.getY()-range,player.getZ()-range,
                        player.getX()+range,player.getY()+range,player.getZ()+range);
                List<Animal> animals=serverLevel.getEntitiesOfClass(Animal.class,aabb);
                for (Animal animal:animals) {
                    if (animal.getAge()<0){
                        animal.ageUp(3);//adds 3 sec (60 ticks) of age, by default, animals take 20 min to grow up
                    }
                    else if (animal.getAge()>0){
                        animal.setAge(Math.max(0,animal.getAge()-20));//by default, animals have a breeding period of 5 min
                    }
                }
            }
        }


    }

    public void rejuvenatingWave(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (player instanceof ServerPlayer && player.tickCount % Config.rejWaveInterval==0){
            Level level =player.level();
            player.heal((float) Config.rejWaveAmt);
            //level.getEntitiesOfClass()
        }
    }
}
