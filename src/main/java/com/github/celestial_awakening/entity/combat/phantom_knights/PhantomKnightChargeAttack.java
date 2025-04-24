package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PhantomKnightChargeAttack extends GenericAbility {
    Vec3 targetDir;
    int maxExecTime;
    public PhantomKnightChargeAttack(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        maxExecTime=executeTime;
    }
    @Override
    public void startAbility(LivingEntity target,double dist){
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            super.startAbility(target,dist);
            this.mob.canMove=false;
            this.targetDir= MathFuncs.getDirVec(this.mob.position(),target.position());
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            List<LivingEntity> entities=this.mob.findCollidedEntities(CA_Predicates.opposingTeams_IgnoreSameClass_Predicate(this.mob));
            for (LivingEntity entity: entities) {
                boolean didHurt=entity.hurt(this.mob.damageSources().mobAttack(this.mob),4.5f);
                if (didHurt){
                    double kbRes = entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                    double kbTaken = Math.max(0.0D, 1.0D - kbRes);

                    entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, (double)0.125F * kbTaken, 0.0D));
                }
            }
            Vec3 newPos=this.mob.position().add(targetDir.scale(1.2f));
            BlockPos blockpos = new BlockPos(Mth.floor(newPos.x), Mth.floor(newPos.y), Mth.floor(newPos.z));
            BlockState blockstate = this.mob.level().getBlockState(blockpos);
            if (!blockstate.isAir()){
                //stop movement, stop ability early
                this.currentStateTimer=1;
            }
            else{
                this.mob.setPos(newPos);
            }

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

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 8.5D;
    }
}
