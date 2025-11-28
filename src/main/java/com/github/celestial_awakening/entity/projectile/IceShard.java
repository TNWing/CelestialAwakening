package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;

public class IceShard extends CA_Projectile{
    protected IceShard(EntityType<? extends Projectile> p_37248_, Level p_37249_, int lt) {
        super(p_37248_, p_37249_, lt);
    }
    public static IceShard create(Level level, int lt, float spd, float hA, float vA, float dmg, @Nullable Entity owner){
        IceShard entity = new IceShard(EntityInit.SHINING_ORB.get(), level,lt);
        entity.setNoGravity(true);
        entity.setMoveValues(spd,hA,vA);
        entity.setDmg(dmg);
        entity.damagesource=new DamageSource(level.damageSources().freeze().typeHolder(),owner);
        entity.setOwner(owner);
        return entity;
    }
    public static IceShard create(Level level, int lt, float spd, float hA, float vA, float dmg, DamageSource source, @Nullable Entity owner){
        IceShard entity = new IceShard(EntityInit.SHINING_ORB.get(), level,lt);
        entity.setNoGravity(true);
        entity.setMoveValues(spd,hA,vA);
        entity.setDmg(dmg);
        entity.damagesource=source;
        entity.setOwner(owner);
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
    protected boolean canHitEntity(Entity p_37250_) {
        return super.canHitEntity(p_37250_) && p_37250_!=this.getOwner();
    }
    public void tick(){
        Entity entity = this.getOwner();

        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            this.calcAndMove();
            super.tick();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, (this::canHitEntity));
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
        } else {
            this.discard();
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity=result.getEntity();
        if (entity.hurt(damagesource,getDmg())){
            if (entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100));
            }
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

}
