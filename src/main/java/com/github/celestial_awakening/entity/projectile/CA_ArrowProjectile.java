package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.AlertInterface;
import com.github.celestial_awakening.entity.CA_Entity;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class CA_ArrowProjectile extends AbstractArrow implements CA_Entity {
    private static final EntityDataAccessor<Integer> ARROW_TYPE = SynchedEntityData.defineId(CA_ArrowProjectile.class, EntityDataSerializers.INT);

    AlertInterface alertInterface;
    ArrowType type;
    ParticleOptions particleOptions;
    boolean hasHitBlock;
    DamageSourceIgnoreIFrames lunarDamage=new DamageSourceIgnoreIFrames(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),this.getOwner(),this);
    public CA_ArrowProjectile(EntityType<CA_ArrowProjectile> customArrowProjectileEntityType, Level level) {
        super(customArrowProjectileEntityType,level);
    }
    public CA_ArrowProjectile(Level p_36866_, LivingEntity p_36867_) {
        super(EntityInit.CUSTOM_ARROW.get(), p_36867_, p_36866_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARROW_TYPE,0);
    }
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ARROW_TYPE,tag.getInt("AType"));

    }
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AType",this.entityData.get(ARROW_TYPE));
    }



    public static CA_ArrowProjectile create(Level level, LivingEntity owner, ArrowType t) {
        CA_ArrowProjectile entity = new CA_ArrowProjectile(level,owner);
        entity.type=t;
        switch(t){
            case SOLAR -> {
                entity.particleOptions =ParticleTypes.FLAME;
                entity.setBaseDamage(Config.arrowSolarDmg);
            }
            case LUNAR -> {
                entity.particleOptions =ParticleTypes.CRIT;
                entity.setBaseDamage(Config.arrowLunarDmg);
            }
            case SINGULARITY -> {
                entity.particleOptions=ParticleTypes.END_ROD;
            }
        }
        return entity;
    }


    protected ArrowType getArrowType() {
        return this.type;
    }

    @Override
    protected ItemStack getPickupItem() {
        switch(type){
            case SOLAR -> {
                return new ItemStack(ItemInit.SOLAR_ARROW.get());
            }
            case LUNAR -> {
                return new ItemStack(ItemInit.LUNAR_ARROW.get());
            }
        }
        return new ItemStack(Items.ARROW);
    }

    public ParticleOptions getParticleOptionsFromType(ArrowType at){
        switch (at){
            case LUNAR -> {
                return ParticleTypes.CRIT;
            }
            case SOLAR -> {
                return ParticleTypes.FLAME;
            }
            case SINGULARITY -> {
                return ParticleTypes.END_ROD;
            }
        }
        return ParticleTypes.CRIT;
    }
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if(!this.inGround && !hasHitBlock){
                this.level().addParticle(getParticleOptionsFromType(ArrowType.values()[this.entityData.get(ARROW_TYPE)]), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }

        }
        else if (!this.inGround && !hasHitBlock){
            if (type==ArrowType.SOLAR){
                AABB aabb=this.getBoundingBox().inflate(1.5f);
                Predicate p=null;
                if (this.getOwner() instanceof LivingEntity){
                    p=CA_Predicates.opposingTeamsPredicate((LivingEntity) this.getOwner());
                }
                List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,aabb,p);
                for (LivingEntity entity:livingEntityList) {
                    entity.setSecondsOnFire(4);
                }
            }
        }
    }


    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (type==ArrowType.LUNAR){
            Vec3 dir=this.getDeltaMovement().normalize();
            dir=new Vec3(dir.x,0,dir.z);
            Vec3 targetPos=this.position();//hitResult.getEntity().getBoundingBox().getCenter();
            AABB aabb=new AABB(targetPos.subtract(0,1,0),targetPos.add(dir.scale(5)).add(0,1,0));
            Predicate p=null;
            if (this.getOwner() instanceof LivingEntity){
                p=CA_Predicates.opposingTeamsPredicate((LivingEntity) this.getOwner());
            }
            List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,aabb,p);
            for (LivingEntity entity:livingEntityList) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,50));
                entity.hurt(lunarDamage,0.7f);
            }
        }
        else if(type==ArrowType.SINGULARITY){

        }
        else if (type==ArrowType.SOLAR){
            hitResult.getEntity().setSecondsOnFire(6);
        }
        super.onHitEntity(hitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        hasHitBlock=true;
    }

    @Override
    public AlertInterface getAlertInterface() {
        return this.alertInterface;
    }

    @Override
    public void setAlertInterface(AlertInterface alertInterface) {
        this.alertInterface=alertInterface;
    }
}
