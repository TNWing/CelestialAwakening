package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.ProjCapS2CPacket;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PK_CrescenciaCrescentWhirlwind extends GenericAbility {
    //for the anim, maybe have
    float currentStartAngle;//angle of first crescent of each burst
    int repsRemaining;//# of crescent bursts
    float horiDiff=1.5f;
    float vertDiff=0.6f;
    float[] crescentDmgMult={0.7f,1f,1.4f};
    float[] whirlwindDmgMult={0.85f,1.1f,1.35f};
    float[] crescentDmgVals={4.5f,6.5f,9f};
    float[] whirlwindDmgVals={5.5f,7f,8.5f};
    DamageSourceIgnoreIFrames whirlwindSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this.mob);
    public PK_CrescenciaCrescentWhirlwind(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime,int pri) {
        super(mob, castTime, CD, executeTime, recoveryTime,pri);
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
            this.mob.setActionId(6);
        }

    }
    @Override
    public boolean otherConds(LivingEntity target){
        return Math.abs(target.position().y-this.mob.position().y)<=1f;
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){
            int diffMod=this.mob.level().getDifficulty().getId();
            if (diffMod>0){
                diffMod-=1;
            }
            if (this.currentStateTimer%10==0){
                double atkPow=mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float dmg= (float) (atkPow*whirlwindDmgMult[diffMod]);
                Vec3 pos=this.mob.position();
                AABB aabb=new AABB(pos.x-horiDiff,1f + pos.y-vertDiff,pos.z-horiDiff,pos.x+horiDiff,1f + pos.y+vertDiff,pos.z+horiDiff);
                List<LivingEntity> entities= this.mob.level().getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeams_IgnoreSameClass_Predicate(this.mob));
                boolean b= this.mob.level().getDifficulty().getId()>1;

                for (LivingEntity entity:entities) {

                    if (b && entity.hurt(whirlwindSource,dmg)){
                        MobEffectInstance curse=new MobEffectInstance(MobEffectInit.MOON_CURSE.get(),600,0);
                        entity.addEffect(curse);
                    }
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
                    this.mob.setActionId(7);
                    break;
                }
                case 1:{
                    changeToState2();
                    this.mob.setActionId(8);
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
        double atkPow=mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float dmg= (float) (atkPow*crescentDmgMult[diffMod]);
        for (int i=0;i<4;i++){
            float ang=currentStartAngle+90*i;
            LunarCrescent crescent=LunarCrescent.create(serverLevel,dmg,85,7f,ang,0,0,1.75f,0.35f,1.75f);
            int id=crescent.getId();
            @NotNull LazyOptional<ProjCapability> capOptional=crescent.getCapability(ProjCapabilityProvider.ProjCap);

            Vec3 dir=MathFuncs.get2DVecFromAngle(ang);
            crescent.setPos(this.mob.position().add(dir.scale(0.2f)).add(0,1.2f,0));
            crescent.setOwner(this.mob);
            crescent.setDisableShields(true);
            crescent.setDisableTicks(40);
            capOptional.ifPresent(cap->{
                MovementModifier deacc_mod=new MovementModifier
                        (MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,0.15f,0,0,15,10);
                MovementModifier flip_mod=new MovementModifier
                        (MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,0,0,-360,0,10);
                MovementModifier acc_mod=new MovementModifier
                        (MovementModifier.modFunction.NUM, MovementModifier.modOperation.MULT,MovementModifier.modFunction.NUM,MovementModifier.modOperation.SET,2.15f,0,0,0,40);
                cap.putInBackOfList(deacc_mod);
                cap.putInBackOfList(flip_mod);
                cap.putInBackOfList(acc_mod);
                ModNetwork.sendToClientsInDim(new ProjCapS2CPacket(id, cap),serverLevel.dimension());
            });
            serverLevel.addFreshEntity(crescent);
            ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),serverLevel.dimension());

        }
        currentStartAngle+=22.5f;
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 5.25D;
    }

    @Override
    public int calcPriority(){
        if (this.getCurrentCD()>0){
            return -1;
        }
        int p= this.getBasePriority();
        List<LivingEntity> list=mob.level().getNearbyEntities(LivingEntity.class,null,mob,new AABB(0,0,0,0,0,0));
        if (list.size()<3){
            //System.out.println("Too few targets, whirlwind less likely");
            p+=20;
        }
        return p;
    }
}
