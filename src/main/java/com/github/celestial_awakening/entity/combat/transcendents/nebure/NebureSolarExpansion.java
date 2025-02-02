package com.github.celestial_awakening.entity.combat.transcendents.nebure;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class NebureSolarExpansion extends GenericAbility {
    Predicate pred= CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate(this.mob,List.of(AbstractTranscendent.class));
    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);
    ParticleOptions particleType = ParticleTypes.LAVA;
    int particleCnt=4;
    float particleSpd=0;
    Vec3 centerPos;
    List<Vec3> particlePos;
    public NebureSolarExpansion(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void executeAbility(LivingEntity target) {

    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 10;
    }

    void calculateWhereToDraw(float oRad,float iRad){

    }

    void drawParticles(ServerLevel lvl){
        for (Vec3 pos:particlePos) {
            lvl.sendParticles(particleType,pos.x,pos.y,pos.z,particleCnt,0,0,0,particleSpd);
        }
    }


    void damageArea(float oRad,float iRad){
        List<LivingEntity> entityList= MathFuncs.getEntitiesIn2DDonutArea(LivingEntity.class,centerPos,this.mob.level(),oRad,iRad,2,pred);
        for (LivingEntity entity :entityList) {
            entity.hurt(source,4f);
        }
    }
}
