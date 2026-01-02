package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SolmanderRekindle extends GenericAbility {
    boolean targetPlayers;
    boolean extinguishFires;
    int hBound;
    int vBound;
    public SolmanderRekindle(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,boolean playerExtinguish, boolean extinguish, int hRange, int vRange) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        targetPlayers=playerExtinguish;
        extinguishFires=extinguish;
        hBound=hRange;
        vBound=vRange;
        this.name="Rekindle";
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            ServerLevel serverLevel= (ServerLevel) this.mob.level();
            for (int x=-hBound;x<=hBound;x++){
                for (int z=-hBound;z<=hBound;z++){
                    for (int y=-vBound;y<=vBound;y++){
                        BlockPos pos=new BlockPos(x,y,z);
                        BlockState blockState= serverLevel.getBlockState(pos);
                        if (blockState.is(BlockTags.FIRE)){
                            if (extinguishFires){
                                serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                            }
                            this.mob.heal(0.4f);
                        }
                    }
                }
            }
            if (targetPlayers){
                AABB aabb=new AABB(this.mob.blockPosition());
                aabb=aabb.inflate(4.5f,1.2f,4.5);

                List<LivingEntity> list=serverLevel.getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnoreSameClass_Predicate(this.mob));
                for (LivingEntity entity:list) {
                    int fTicks=entity.getRemainingFireTicks();
                    if (extinguishFires){
                        entity.setRemainingFireTicks(0);
                    }
                    this.mob.heal(fTicks*0.25f);

                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int)(fTicks*1.5)));
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (int)(fTicks*2)));
                }
            }
        }
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    break;
                }
                case 1:{
                    break;
                }
                case 2:{
                    break;
                }
            }
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
