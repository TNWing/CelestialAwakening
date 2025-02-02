package com.github.celestial_awakening.entity.combat.transcendents.astralite;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.init.MobEffectInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class AstralitePhotonSurge extends GenericAbility {
    private BlockPos targetBlockPos;
    public AstralitePhotonSurge(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }
    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.mob);
    ParticleOptions particleType = ParticleTypes.FALLING_LAVA;
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange = Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            mob.canMove=false;
            super.startAbility(target,dist);
            setMoveVals(this.getAbilityRange(target),this.getAbilityRange(target),false);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {

        switch (state){
            case 0:{
                break;
            }
            case 1:{
                if (this.currentStateTimer%30==29){
                    Vec3 targetPos=target.position();
                    targetBlockPos= BlockPos.containing(targetPos);
                }
                Level level=target.level();
                if (!level.isClientSide() && targetBlockPos!=null){
                    ServerLevel serverLevel= (ServerLevel) level;
                    if (this.currentStateTimer%15<=10){
                        burnGround(serverLevel);
                    }
                    else{
                        makeParticles(serverLevel);
                    }
                }
                break;
            }
            case 2:{
                break;
            }
        }

        if (currentStateTimer<=0){
            switch (state){
                case 0:{
                    changeToState1();
                    mob.canMove=false;
                    break;
                }
                case 1:{
                    changeToState2();
                    break;
                }
                case 2:{
                    state=0;
                    liftRestrictions();
                    break;
                }
            }
        }
        this.currentStateTimer--;

    }

    private void makeParticles(ServerLevel serverLevel){//15 ticks of particle
        int x= targetBlockPos.getX();
        int y= targetBlockPos.getY();
        int z= targetBlockPos.getZ();
        int count=20;
        float speed=0.15f;
        serverLevel.sendParticles(particleType, x, y, z, count, 0, 1, 0, speed);
    }


    private void burnGround(ServerLevel serverLevel){//every 15 ticks, burn ground
        int x= targetBlockPos.getX();
        int y= targetBlockPos.getY();
        int z= targetBlockPos.getZ();
        int count=25;
        float speed=0.45f;
        serverLevel.sendParticles(particleType, x, y, z, count, 0, 3, 0, speed);
        AABB aabb=new AABB(x,y,z,x+1,y+4,z+1);
        Predicate predicate= o -> !(o instanceof AbstractTranscendent);
        List<LivingEntity> entities= serverLevel.getEntitiesOfClass(LivingEntity.class,aabb,predicate);
        for (LivingEntity entity:entities) {

            source.invulTicks=entity.invulnerableTime;
            entity.hurt(source,0.5f);
            MobEffectInstance purgingLight=new MobEffectInstance(MobEffectInit.PURGING_LIGHT.get(),130,0);
            entity.addEffect(purgingLight);
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 8f;
    }
}
