package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.ToolTipBuilder;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;

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
    public void onLivingDeath(LivingDeathEvent event,Player player,int cnt){
        pieceEffect_Death(event,cnt);
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
        ToolTipBuilder.addShiftInfo(event);
        ToolTipBuilder.addFullSetName(event,PARTICLE_NAME,boldColor);
        ToolTipBuilder.addFullSetName(event,WAVE_NAME,boldColor);
        ToolTipBuilder.addPieceBonusName(event,ABS_NAME,boldColor);
    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {
        ToolTipBuilder.addFullArmorSetComponent(event,PARTICLE_NAME,boldColor,PARTICLE_DESC,infoColor);
        ToolTipBuilder.addFullArmorSetComponent(event,WAVE_NAME,boldColor,WAVE_DESC,infoColor);
        ToolTipBuilder.addArmorPieceComponent(event,ABS_NAME,boldColor,ABS_DESC,infoColor);
    }
    public void pieceEffect_Crop(BlockEvent.BreakEvent event, int cnt){
        if (!event.getLevel().isClientSide() && event.getState().getBlock() instanceof CropBlock && !event.isCanceled()){
            if (random.nextInt(100)<cnt*2){
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
    public void pieceEffect_Death(LivingDeathEvent deathEvent, int cnt){
        if (deathEvent.getSource().getEntity() !=  null & deathEvent.getSource().getEntity()  instanceof Player && deathEvent.getEntity() instanceof Animal){
            Animal animal= (Animal) deathEvent.getEntity();
            if (random.nextInt(100)<cnt*4.5f){
                Level level=animal.level();
                BlockPos blockPos=animal.blockPosition();
                ItemEntity itemEntity;
                int rollNum=random.nextInt(0,11);
                if (rollNum<3){
                    int amt=random.nextInt(1,4);
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(Items.BONE,amt));
                }
                else if (rollNum<8){
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.SUNSTONE.get()));
                }
                else{
                    itemEntity=new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.LIFE_FRAG.get()));
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
                        BlockPos blockPos=player.blockPosition();
                        blockPos=blockPos.offset(x,y,z);
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
