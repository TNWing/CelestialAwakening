package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.joml.Vector3f;

import java.util.function.Predicate;

public class OrbiterProjectile extends CA_Projectile {
    private int life;
    Predicate pred;
    private static final EntityDataAccessor<Integer> RELEASETIMER= SynchedEntityData.defineId(ShiningOrb.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> H_ANGLE= SynchedEntityData.defineId(ShiningOrb.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> V_ANGLE= SynchedEntityData.defineId(ShiningOrb.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> S_ANGLE= SynchedEntityData.defineId(ShiningOrb.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> OWNER_PREV_POS= SynchedEntityData.defineId(ShiningOrb.class, EntityDataSerializers.VECTOR3);
    public OrbiterProjectile(EntityType<OrbiterProjectile> orbiterProjectileEntityType, Level level) {
        super(EntityInit.ORBITER.get(),level,100);
        this.setDmg(2.5f);
    }
    public static OrbiterProjectile create(Level level, LivingEntity owner,int timer) {
        OrbiterProjectile entity=new OrbiterProjectile(EntityInit.ORBITER.get(),level);
        entity.setOwner(owner);
        entity.setDmg(2.5f);
        Vec3 ownerPos=entity.getOwner().position();
        double hRad=Math.toRadians(entity.getEntityData().get(H_ANGLE));
        double vRad=Math.toRadians(entity.getEntityData().get(V_ANGLE));
        Vec3 newPos=new Vec3(ownerPos.x + Math.sin(hRad)*1.4f,ownerPos.y + Math.sin(vRad)*0.6f,ownerPos.z+Math.cos(hRad)*1.4f);
        entity.setPos(newPos);
        entity.entityData.set(OWNER_PREV_POS,owner.position().toVector3f());
        entity.entityData.set(RELEASETIMER,timer);
        entity.setNoGravity(true);
        return entity;
    }
    @Override
    public void setOwner(Entity ent){
        super.setOwner(ent);
        setUpPred();
    }

    public void setUpPred(){
        Entity owner=this.getOwner();
        pred= o -> {
            if (o instanceof LivingEntity && owner!=null){
                LivingEntity livingEntity= (LivingEntity) o;
                Team team=livingEntity.getTeam();
                Team ownerTeam=owner.getTeam();
                if (team==null || ownerTeam==null){
                    return true;
                }
                return !ownerTeam.equals(team);

            }
            return false;
        };
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RELEASETIMER,120);
        this.entityData.define(H_ANGLE,0f);
        this.entityData.define(V_ANGLE,0f);
        this.entityData.define(S_ANGLE,0f);
        this.entityData.define(OWNER_PREV_POS,new Vector3f());
    }


    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity != null && !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            calcAndMove();

            if (this.entityData.get(RELEASETIMER)>0 && this.getOwner()!=null){
                orbit();
                this.entityData.set(RELEASETIMER,this.entityData.get(RELEASETIMER)-1);
            }
            else if (this.entityData.get(RELEASETIMER)==0 && this.getOwner()!=null){
                release();
                this.entityData.set(RELEASETIMER,this.entityData.get(RELEASETIMER)-1);
            }
            else{//handle movement here
                checkCollision();
                this.checkInsideBlocks();
                this.life++;
                if (this.life>=this.getLifeTime()){
                    this.discard();
                }
            }
            super.tick();
        }
        else {
            this.discard();
        }

    }
    public void orbit(){
        Vec3 ownerPos=getOwner().position();//current pos

        float t=0.1f;
        Vec3 ownerPrevPos=MathFuncs.fromVector3f(this.entityData.get(OWNER_PREV_POS));
        Vec3 interpolatedPosition = ownerPrevPos.add(
                (ownerPos.x - ownerPrevPos.x) * t,
                (ownerPos.y - ownerPrevPos.y) * t,
                (ownerPos.z - ownerPrevPos.z) * t);

        double hRad=Math.toRadians(this.getEntityData().get(H_ANGLE));
        double vRad=Math.toRadians(this.getEntityData().get(V_ANGLE));
        Vec3 newPos=new Vec3(interpolatedPosition.x + Math.sin(hRad)*1.4f,interpolatedPosition.y + Math.sin(vRad)*0.6f+0.7f,interpolatedPosition.z+Math.cos(hRad)*1.4f);
        this.setPos(newPos);
        this.entityData.set(H_ANGLE,this.entityData.get(H_ANGLE)+5f);
        this.entityData.set(V_ANGLE,this.entityData.get(V_ANGLE)+10f);
        this.entityData.set(S_ANGLE,this.entityData.get(S_ANGLE)+25f);
        this.entityData.set(OWNER_PREV_POS,ownerPos.toVector3f());
    }

    public void release(){

        TargetingConditions conds=TargetingConditions.forCombat().range(6).ignoreInvisibilityTesting().selector(pred);
        AABB aabb=this.getBoundingBox().inflate(7,2,7);
        LivingEntity target=this.findNearestEntity(conds,aabb);
        if (target!=null){
            Vec3 targetPos=target.position();
            Vec3 dir=targetPos.subtract(this.position()).normalize();
            float hA=MathFuncs.getAngFrom2DVec(dir);
            float vA=MathFuncs.getVertAngFromVec(dir);
            this.setMoveValues(1.6f,hA,vA);
        }
        else{
            this.discard();
        }

    }

    protected void checkCollision(){
        TargetingConditions conds=TargetingConditions.forCombat().range(6).ignoreInvisibilityTesting().selector(pred);
        LivingEntity entity=this.entityToHurt(conds);
        Entity owner=this.getOwner();
        if (entity!=null){
            if (owner == null) {
                damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),null);
            }

            else {
                damagesource=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), owner);

            }
            if (entity.hurt(damagesource, this.getDmg())) {
                float ang= this.getHAng();
                double xDir=Math.sin(ang);
                double yDir=Math.cos(ang);
                entity.knockback(1.75f,xDir,yDir);
                if (owner!=null && owner instanceof LivingEntity) {
                    ((LivingEntity)owner).setLastHurtMob(entity);
                }
                //this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            }
        }
    }

}
