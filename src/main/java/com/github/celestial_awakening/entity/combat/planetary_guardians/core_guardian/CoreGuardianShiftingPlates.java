package com.github.celestial_awakening.entity.combat.planetary_guardians.core_guardian;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.planetary_guardians.CoreGuardian;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreGuardianShiftingPlates extends GenericAbility {
    int updatePlateDelay=5;
    ParticleOptions particleType = (ParticleOptions) ParticleTypes.FALLING_DUST;
    ArrayList<BlockPos> blockPosList=new ArrayList<>();
    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FLY_INTO_WALL),this.mob);
    public CoreGuardianShiftingPlates(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime, int basePriority) {
        super(mob, castTime, CD, executeTime, recoveryTime, basePriority);
    }

    //execute just creates the plates, triggerQuake is called in combataigoal to actually use the plates
    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            if (blockPosList.size()>10){
                return;
            }
            ArrayList<BlockPos> potentialPos = new ArrayList<>();
            for (int x=-7;x<=7;x++){
                for (int z=-7;z<=7;z++){
                    for (int y=-1;y<=1;y++){
                        BlockPos pos=this.mob.blockPosition().offset(x,y,z);
                        if (this.mob.level().getBlockState(pos.above()).isAir()){
                            potentialPos.add(pos);
                            break;
                        }
                    }
                }
            }
            if (potentialPos.size()>0){
                for (int i=0;i<5;i++){
                    if (blockPosList.size()>10){
                        break;
                    }
                    BlockPos pos=potentialPos.get(this.mob.getRandom().nextInt(potentialPos.size()));
                    blockPosList.add(pos);
                    blockPosList.remove(pos);
                }
            }
        }
        this.currentStateTimer--;
        if (this.currentStateTimer<=0){
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

    public void updatePlates(){
        Iterator<BlockPos> iter=blockPosList.iterator();
        while (iter.hasNext()){
            BlockPos blockPos=iter.next();
            if (this.mob.level().getBlockState(blockPos).isAir()){
                List<LivingEntity> entityList=this.mob.level().getEntitiesOfClass(LivingEntity.class,new AABB(blockPos).inflate(1.25f,0.3f,1.25f));
                for (LivingEntity entity:entityList) {
                    if (entity instanceof CoreGuardian coreGuardian){
                        coreGuardian.decrementHardenStacks();
                    }
                    entity.hurt(source,1.5f);
                    iter.remove();
                    break;
                }
            }
            else if (updatePlateDelay==0){
                Vec3 pos=blockPos.getCenter();

                ServerLevel serverLevel= (ServerLevel) this.mob.level();
                serverLevel.sendParticles(particleType, pos.x(),pos.y()+0.4f,pos.z, 20, 0, 0, 0,0.1f);
                List<LivingEntity> entityList=this.mob.level().getEntitiesOfClass(LivingEntity.class,new AABB(blockPos).inflate(0.5f,0.1f,0.5f));
                for (LivingEntity entity:entityList) {
                    entity.hurt(source,0.75f);
                   }
            }
        }
        if (updatePlateDelay>0){
            updatePlateDelay--;
        }
        else{
            updatePlateDelay=5;
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
