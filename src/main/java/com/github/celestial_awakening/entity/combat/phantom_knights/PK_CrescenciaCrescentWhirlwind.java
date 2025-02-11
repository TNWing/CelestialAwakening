package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.networking.packets.ProjCapS2CPacket;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PK_CrescenciaCrescentWhirlwind extends GenericAbility {
    float currentStartAngle;//angle of first crescent of each burst
    int repsRemaining;//# of crescent bursts
    float horiDiff=1.5f;
    float vertDiff=0.3f;
    float[] crescentDmgVals={3.5f,5f,7.5f};
    float[] whirlwindDmgVals={3f,4f,6f};
    float width=1f;
    float height=0.2f;
    float depth=0.4f;
    DamageSourceIgnoreIFrames whirlwindSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this.mob);
    public PK_CrescenciaCrescentWhirlwind(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        this.name="Crescent Whirlwind";
    }
    @Override
    public void startAbility(LivingEntity target,double dist) {
        double abilityRange= Math.pow(this.getAbilityRange(target),2);
        if (abilityRange>=dist){
            this.mob.getDirection();
            this.mob.canMove=false;
            super.startAbility(target,dist);
            repsRemaining=7;
            Vec3 dir=MathFuncs.getDirVec(this.mob.position(),target.position());
            currentStartAngle=MathFuncs.getAngFrom2DVec(dir);
        }

    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            int diffMod=this.mob.level().getDifficulty().getId();
            if (diffMod>0){
                diffMod-=1;
            }
            if (this.currentStateTimer%15==0){
                Vec3 pos=this.mob.position();
                AABB aabb=new AABB(pos.x-horiDiff,pos.y-vertDiff,pos.z-horiDiff,pos.x+horiDiff,pos.y+vertDiff,pos.z+horiDiff);
                List<LivingEntity> entities= this.mob.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnoreSameClass_Predicate(this.mob));
                for (LivingEntity entity:entities) {
                    entity.hurt(whirlwindSource,whirlwindDmgVals[diffMod]);
                }
            }

            if (this.currentStateTimer<300 && this.currentStateTimer%25==0){
                crescentSpawn(diffMod);
            }
        }
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    changeToState1();
                    this.mob.canMove=true;
                    this.mob.spdMod=0.4f;
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
    }

    public void crescentSpawn(int diffMod){
        ServerLevel serverLevel= (ServerLevel) this.mob.level();
        for (int i=0;i<4;i++){
            float ang=currentStartAngle+90*i;
            LunarCrescent crescent=new LunarCrescent(serverLevel,crescentDmgVals[diffMod],100,7f,ang,0,0,1.75f,0.35f,1.75f);
            int id=crescent.getId();
            ProjCapability cap=crescent.getCapability(ProjCapabilityProvider.ProjCap).orElse(null);
            if (cap!=null){
                MovementModifier deacc_mod=new MovementModifier
                        (MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,0.15f,0,0,15,10);
                MovementModifier flip_mod=new MovementModifier
                        (MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,0,0,180,0,20);
                MovementModifier acc_mod=new MovementModifier
                        (MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,2.15f,0,0,0,40);
                cap.putInBackOfList(deacc_mod);
                cap.putInBackOfList(flip_mod);
                cap.putInBackOfList(acc_mod);
            }
            Vec3 dir=MathFuncs.get2DVecFromAngle(ang);
            crescent.setPos(this.mob.position().add(dir.scale(0.2f)));
            crescent.setOwner(this.mob);
            serverLevel.addFreshEntity(crescent);
            ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),serverLevel.dimension());
            ModNetwork.sendToClientsInDim(new ProjCapS2CPacket(id, cap),serverLevel.dimension());
        }
        currentStartAngle+=22.5f;
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 9.5D;
    }
}
