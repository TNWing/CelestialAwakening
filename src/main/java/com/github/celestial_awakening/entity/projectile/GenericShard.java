package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class GenericShard extends CA_Projectile{
    private static final EntityDataAccessor<Integer> SHARD_TYPE = SynchedEntityData.defineId(GenericShard.class, EntityDataSerializers.INT);

    public GenericShard(EntityType<GenericShard> genericShardEntityType, Level level) {
        super(genericShardEntityType,level,120);
    }
    public GenericShard(EntityType<GenericShard> p_37248_, Level p_37249_, int lt) {
        super(p_37248_, p_37249_, lt);
    }
    public enum Type{
        ICE,
        EARTH,
        MOON
    }


    public void setShardType(Type shardType) {
        this.entityData.set(SHARD_TYPE,shardType.ordinal());
    }
    public Type getShardType(){
        return Type.values()[this.entityData.get(SHARD_TYPE)];
    }

    public static GenericShard create(Level level,  Type shardType,int lt, float spd, float hA, float vA, float dmg, DamageSource source, @Nullable Entity owner){
        GenericShard entity = new GenericShard(EntityInit.GENERIC_SHARD.get(), level,lt);
        entity.setNoGravity(true);
        entity.setShardType(shardType);
        entity.setMoveValues(spd,hA,vA);
        entity.setDmg(dmg);
        entity.damagesource=source;
        entity.setOwner(owner);
        entity.setNoGravity(true);
        return entity;
    }
    public static GenericShard create(Level level,  Type shardType,int lt, float spd, float hA, float vA, float dmg, DamageSource source, @Nullable Entity owner,boolean grav){
        GenericShard entity = new GenericShard(EntityInit.GENERIC_SHARD.get(), level,lt);
        entity.setNoGravity(true);
        entity.setShardType(shardType);
        entity.setMoveValues(spd,hA,vA);
        entity.setDmg(dmg);
        entity.damagesource=source;
        entity.setOwner(owner);
        entity.setNoGravity(grav);
        System.out.println("ENTITY NO GRAV? " + entity.isNoGravity());
        return entity;
    }
    @Override
    public void setOwner(Entity e) {
        super.setOwner(e);
        if (e instanceof LivingEntity) {
            pred = CA_Predicates.opposingTeams_IgnoreSameClass_Predicate((LivingEntity) e);
        }
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHARD_TYPE,0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(SHARD_TYPE,tag.getInt("ShardType"));
        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ShardType",this.entityData.get(SHARD_TYPE));
    }
    public void tick(){
        Entity entity = this.getOwner();//

        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            this.calcAndMove();
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, (this::canHitEntity));
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            this.setCurrentLifetime(this.getCurrentLifeTime()+1);
            if (this.getCurrentLifeTime()>=this.getLifeTime()){
                this.discard();
            }
        } else {
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity p_37250_) {
        return super.canHitEntity(p_37250_) && p_37250_!=this.getOwner();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity=result.getEntity();
        System.out.println("HELLLO ENT " + entity);
        if (damagesource!=null){
            if (entity.hurt(damagesource,getDmg())){
                if (entity instanceof LivingEntity livingEntity){
                    onHitEffects(livingEntity);
                }
            }
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        System.out.println("HIT BLOCK");
        this.discard();
    }

    void onHitEffects(LivingEntity entity) {
        switch(getShardType()){
            case ICE :{
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100));
                break;
            }
            case MOON:{
                break;
            }
            case EARTH:{
                break;
            }
        }
    }
}
