package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.AlertInterface;
import com.github.celestial_awakening.entity.CA_Entity;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
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



    public static CA_ArrowProjectile create(Level level, LivingEntity owner, ArrowType t) {
        CA_ArrowProjectile entity = new CA_ArrowProjectile(level,owner);
        entity.type=t;
        switch(t){
            case SOLAR -> {
                entity.particleOptions =ParticleTypes.FLAME;
                entity.setBaseDamage(1.5f);
            }
            case LUNAR -> {
                entity.particleOptions =ParticleTypes.CRIT;
                entity.setBaseDamage(1.8f);
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

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if(!this.inGround && !hasHitBlock){
                this.level().addParticle(particleOptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
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
            //dir=new Vec3(dir.x,0,dir.z);
            Vec3 targetPos=hitResult.getEntity().getBoundingBox().getCenter();
            targetPos=this.position();
            AABB aabb=new AABB(targetPos,targetPos.add(dir.scale(3)));
            //hitResult.getEntity().position()
            Predicate p=null;
            if (this.getOwner() instanceof LivingEntity){
                p=CA_Predicates.opposingTeamsPredicate((LivingEntity) this.getOwner());
            }
            List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,aabb,p);//why empty?
            for (LivingEntity entity:livingEntityList) {
                System.out.println("entity is " + entity);
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,50));
                entity.hurt(lunarDamage,0.7f);
            }
        }
        else if(type==ArrowType.SINGULARITY){

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
