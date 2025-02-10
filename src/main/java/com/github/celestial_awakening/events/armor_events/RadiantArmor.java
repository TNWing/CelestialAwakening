package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.Random;

public class RadiantArmor extends ArmorEffect {
    Random random=new Random();
    int boldColor=0x006700;
    int infoColor=0x00b300;
    @Override
    public void performActions(Player player, int cnt, Event event) {
        if (event instanceof BlockEvent.BreakEvent){
            pieceEffect_Crop((BlockEvent.BreakEvent) event,cnt);
        }
        else if (event instanceof LivingDeathEvent){
            pieceEffect_Death((LivingDeathEvent) event,cnt);
        }
        else if (event instanceof TickEvent.PlayerTickEvent){
            excitedParticles((TickEvent.PlayerTickEvent) event);
            rejuvenatingWave((TickEvent.PlayerTickEvent) event);
        }
    }

    @Override
    public void onItemTooltipEvent(ItemTooltipEvent event, int cnt) {


    }
    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,"Excited Particles",boldColor);
        ToolTipBuilder.addFullSetName(event,"Rejuvinating Wave",boldColor);
        ToolTipBuilder.addPieceBonusName(event,"Life Absorption",boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,"Excited Particles",boldColor,"Speeds up the growth of nearby plants as well as speeds up the growth and breeding recovery period of animals.",infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,"Rejuvinating Wave",boldColor,"Every 7 seconds, recover 2.5 HP.",infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,"Life Absorption",boldColor,"Obtain special materials upon gathering crops or killing animals.",infoColor);
    }
    public void pieceEffect_Crop(BlockEvent.BreakEvent event, int cnt){
        if (!event.getLevel().isClientSide() && event.getState().getBlock() instanceof CropBlock && !event.isCanceled()){
            if (random.nextInt(100)<cnt*2){
                Player player=event.getPlayer();
                Level level=player.level();
                BlockPos blockPos=player.blockPosition();
                ItemEntity itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.SUNSTONE.get()));
                level.addFreshEntity(itemEntity);
            }

        }
    }
    public void pieceEffect_Death(LivingDeathEvent deathEvent, int cnt){
        if (deathEvent.getSource().getDirectEntity() !=  null & deathEvent.getSource().getDirectEntity()  instanceof Player && deathEvent.getEntity() instanceof Animal){
            Animal animal= (Animal) deathEvent.getEntity();
            if (random.nextInt(100)<cnt*4.5f){
                Level level=animal.level();
                BlockPos blockPos=animal.blockPosition();
                ItemEntity itemEntity;
                int rollNum=random.nextInt(0,10);
                if (rollNum<5){
                    int amt=random.nextInt(1,4);
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(Items.BONE,amt));
                }
                else{
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.SUNSTONE.get()));
                }

                level.addFreshEntity(itemEntity);
            }
        }
    }

    public void excitedParticles(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (player instanceof ServerPlayer && player.tickCount % 50==0){
            ServerLevel serverLevel= (ServerLevel) player.level();
            for (int x=-3;x<=3;x++){
                for (int z=-3;z<=3;z++){
                    for (int y=-3;y<=3;y++){
                        BlockPos blockPos=new BlockPos(x,y,z);
                        BlockState blockState=serverLevel.getBlockState(blockPos);
                        Block block=blockState.getBlock();
                        if (block instanceof CropBlock||
                        block instanceof SaplingBlock){
                            System.out.println("OUR BLOCK IS "  + block.getName());
                            blockState.randomTick(serverLevel,blockPos,serverLevel.random);
                        }
                    }
                }
            }
            AABB aabb=new AABB(player.getX()-3,player.getY()-3,player.getZ()-3,player.getX()+3,player.getY()+3,player.getZ()+3);
            List<Animal> animals=serverLevel.getEntitiesOfClass(Animal.class,aabb);
            for (Animal animal:animals) {
                System.out.println(animal);
                if (animal.getAge()<0){
                    animal.ageUp(1);//adds 1 sec (20 ticks) of age
                }
                else if (animal.getAge()>0){
                    animal.setAge(animal.getAge()-20);
                }
                //goes from -X to 0, 0 is adult, negative num is child
                //positive age means animal is on breeding cooldown, cooldown starts at 6k ticks
            }
        }

    }

    public void rejuvenatingWave(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (player instanceof ServerPlayer && player.tickCount % 140==0){
            Level level =player.level();
            player.heal(2.5f);
            //level.getEntitiesOfClass()
        }
    }
}
