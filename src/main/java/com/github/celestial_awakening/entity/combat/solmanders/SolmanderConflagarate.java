package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SolmanderConflagarate extends GenericAbility {
    int hBound;
    int vBound;
    boolean diagonal=false;
    public SolmanderConflagarate(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if(state==1){
            conflagarate();
        }
        this.currentStateTimer--;
        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    break;
                }
                case 1:{
                    state++;
                    currentStateTimer=abilityRecoveryTime;

                    break;
                }
                case 2:{
                    state=0;
                    liftRestrictions();
                    break;
                }
            }
        }
    }

    void conflagarate(){
        ServerLevel serverLevel= (ServerLevel) this.mob.level();
        for (int x=-hBound;x<=hBound;x++){
            for (int z=-hBound;z<=hBound;z++){
                for (int y=-vBound;y<=vBound;y++){
                    BlockPos pos=new BlockPos(x,y,z);
                    BlockState blockState= serverLevel.getBlockState(pos);
                    Block block=blockState.getBlock();

                    if (blockState.is(BlockTags.FIRE)){

                        if (diagonal){

                        }
                        for (int fx=-1;fx<=1;fx+=2){
                            BlockPos nearbyPos=pos.offset(fx,1,0);
                            if (BaseFireBlock.canBePlacedAt(serverLevel,nearbyPos,Direction.UP)){
                                BlockState stateVals= BaseFireBlock.getState(serverLevel,nearbyPos);
                                BlockState newFire=block.defaultBlockState()
                                        .setValue(FireBlock.NORTH,stateVals.getValue(FireBlock.NORTH))
                                        .setValue(FireBlock.SOUTH,stateVals.getValue(FireBlock.SOUTH))
                                        .setValue(FireBlock.EAST,stateVals.getValue(FireBlock.EAST))
                                        .setValue(FireBlock.WEST,stateVals.getValue(FireBlock.WEST))
                                        .setValue(FireBlock.UP,stateVals.getValue(FireBlock.UP));
                                serverLevel.setBlockAndUpdate(nearbyPos, newFire);
                                //serverLevel.setBlockAndUpdate(nearbyPos, BaseFireBlock.getState(serverLevel,blockState.getBlock().getStateForPlacement(serverLevel,nearbyPos)));
                            }
                            /*
                            BlockState nearbyState=serverLevel.getBlockState(nearbyPos);


                            if (!nearbyState.isAir()){

                            }
                            if (nearbyState.isAir()){
                                serverLevel.setBlock(nearbyPos,blockState,3);
                            }
                            else{
                                for (int fy=-2;fy<=2;fy++){
                                    if (fy!=0){

                                    }
                                }
                                //attempt to create fire above it
                            }

                             */
                        }
                        for (int fz=-1;fz<=1;fz+=2){
                            BlockPos nearbyPos=pos.offset(0,1,fz);
                            if (BaseFireBlock.canBePlacedAt(serverLevel,nearbyPos,Direction.UP)){
                                BlockState stateVals= BaseFireBlock.getState(serverLevel,nearbyPos);
                                BlockState newFire=block.defaultBlockState()
                                        .setValue(FireBlock.NORTH,stateVals.getValue(FireBlock.NORTH))
                                        .setValue(FireBlock.SOUTH,stateVals.getValue(FireBlock.SOUTH))
                                        .setValue(FireBlock.EAST,stateVals.getValue(FireBlock.EAST))
                                        .setValue(FireBlock.WEST,stateVals.getValue(FireBlock.WEST))
                                        .setValue(FireBlock.UP,stateVals.getValue(FireBlock.UP));
                                serverLevel.setBlockAndUpdate(nearbyPos, newFire);
                                //serverLevel.setBlockAndUpdate(nearbyPos, BaseFireBlock.getState(serverLevel,blockState.getBlock().getStateForPlacement(serverLevel,nearbyPos)));
                            }
                        }
                        //for (serverLevel.blo)
                    }
                }
            }
        }
        AABB aabb=new AABB(this.mob.blockPosition());
        aabb=aabb.inflate(9,2.5f,9);
        List<LivingEntity> entityList=serverLevel.getEntitiesOfClass(LivingEntity.class,aabb);
        for (LivingEntity entity:entityList) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks()+80);

        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
