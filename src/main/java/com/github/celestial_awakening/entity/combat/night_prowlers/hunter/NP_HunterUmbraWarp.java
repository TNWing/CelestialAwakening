package com.github.celestial_awakening.entity.combat.night_prowlers.hunter;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NP_HunterUmbraWarp extends GenericAbility {

    DamageSourceIgnoreIFrames slamSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);
    boolean hasPrevTPAllies=false;

    public NP_HunterUmbraWarp(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime, int p) {
        super(mob, castTime, CD, executeTime, recoveryTime,p);
        hasPrevTPAllies=false;
    }

    @Override
    public void executeAbility(LivingEntity target) {
        switch (state){
            case 0:{
                break;
            }
            case 1:{

            }
            case 2:{
                break;
            }
        }
        if (--this.currentStateTimer<=0){
            switch (state){
                case 0:{
                    state++;
                    currentStateTimer=abilityExecuteTime;
                    List<AbstractNightProwler> allies=new ArrayList<>();
                    boolean tpAllies=false;
                    if (!allies.isEmpty() && !hasPrevTPAllies){
                        tpAllies=this.mob.getRandom().nextBoolean();
                    }
                    if (tpAllies){
                        hasPrevTPAllies=true;
                        for (AbstractNightProwler prowler:allies) {
                            prowler.setPos(this.mob.position());
                        }
                    }
                    else{
                        hasPrevTPAllies=false;
                        //teleport to arget
                        Vec3 tpPos=target.position().add(0,4,0);
                        //make sure to find if there is space for the prowler
                    }

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
        return 9;
    }
    /*
    basically, deal damage during execution, then another instance at the end of recovery time
     */
}
